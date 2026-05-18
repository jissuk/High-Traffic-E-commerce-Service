package kr.hhplus.be.server.user.application.usecase.integration;

import kr.hhplus.be.server.user.application.usecase.command.UserCommand;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.application.usecase.ChargePointUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("유저 관련 통합 테스트")
public class ChargePointUseCaseTest {

    @Autowired private ChargePointUseCase chargePointUseCase;
    @Autowired private JpaUserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        initTestData();
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {
        @Test
        @DisplayName("동시에 10건의 충전 요청을 보낼 경우 1번만 성공")
        void 유저_동시성() throws Exception {
            // given
            long userId = savedUser.getId();
            long chargePoint = 10_000L;
            long resultPoint = savedUser.getPoint() + chargePoint;
            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        // when
                        UserCommand command = new UserCommand(userId, chargePoint);
                        chargePointUseCase.execute(command);
                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }
            latch.await();
            executor.shutdown();

            AtomicLong successCount = new AtomicLong();
            AtomicLong failureCount = new AtomicLong();

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount.getAndIncrement();
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount.getAndIncrement();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            User user = userRepository.findById(userId).get();
            assertAll(
                    ()-> assertThat(successCount.get())
                            .as("성공한 요청 수")
                            .isEqualTo(1L),
                    ()-> assertThat(failureCount.get())
                            .as("실패한 요청 수")
                            .isEqualTo(9L),
                ()-> assertThat(user.getPoint()).isEqualTo(resultPoint)
            );
        }
    }

    private void initTestData() {
        User user = User.builder()
                .point(40000L)
                .build();
        savedUser = userRepository.save(user);
    }
}