package kr.hhplus.be.server.coupon.usecase.integration;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 통합 테스트")
public class IssueCouponUseCaseTest {
    
    @Autowired
    private IssueCouponUseCase issueCouponUseCase;
    @Autowired
    private JpaUserCouponRepository jpaUserCouponRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @BeforeEach
    void setUp() {
        initTestData();
    }

    private void initTestData() {
        UserEntity user = jpaUserRepository.save(UserStep.유저엔티티_기본값());
        // 쿠폰 수량 : 500개
        CouponEntity coupon = jpaCouponRepository.save(CouponStep.쿠폰엔티티_기본값());
        jpaUserCouponRepository.save(CouponStep.유저쿠폰엔티티_기본값(user, coupon));
    }
    @Nested
    @DisplayName("성공 케이스")
    class success {
        @Test
        @DisplayName("동시에 100건의 실시간쿠폰발급 요청을 보낼 경우 모두 성공")
        void 실시간쿠폰발급_분산락_동시성() throws Exception {
            int threadCount = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        issueCouponUseCase.execute(CouponStep.유저쿠폰커맨드_기본값());
                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }
            latch.await();
            executor.shutdown();

            // then
            CouponEntity coupon = jpaCouponRepository.findById(1L).get();
            assertThat(coupon.getQuantity()).isEqualTo(400L);
        }
    }
}
