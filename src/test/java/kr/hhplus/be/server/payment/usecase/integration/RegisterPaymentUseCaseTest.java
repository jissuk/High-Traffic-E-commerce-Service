package kr.hhplus.be.server.payment.usecase.integration;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.PaymentRequestUseCase;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("결제 관련 통합 테스트")
public class RegisterPaymentUseCaseTest {
    @Autowired
    private PaymentRequestUseCase registerPaymentUseCase;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaOrderRepository jpaOrderRepository;
    @Autowired
    private JpaOrderItemRepository jpaOrderItemRepository;
    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE orders;");
        jdbcTemplate.execute("TRUNCATE TABLE order_items;");
        jdbcTemplate.execute("TRUNCATE TABLE payments;");
    }

    private void initTestData() {
        User user = UserStep.defualtUser();
        Order order = OrderStep.orderWithUserId(user);
        OrderItem orderItem = OrderStep.defaultOrderItem(order);
        Payment payment = PaymentStep.paymentWithOrderId(order);

        jpaUserRepository.save(user);
        jpaOrderRepository.save(order);
        jpaOrderItemRepository.save(orderItem);
        jpaPaymentRepository.save(payment);
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {
        @Test
        @DisplayName("동시에 10건의 결제 요청을 보낼 경우 1건만 성공")
        void 결제_동시성() throws Exception {
            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            PaymentCommand command = PaymentStep.defaultPaymentCommand();
            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        registerPaymentUseCase.execute(command);
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
                    if (e.getCause() instanceof RuntimeException) {
                        failureCount.getAndIncrement();
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertAll(
                ()-> assertThat(successCount.get())
                        .as("성공한 요청 수")
                        .isEqualTo(1L),
                ()-> assertThat(failureCount.get())
                        .as("실패한 요청 수")
                        .isEqualTo(9L)
            );
        }
    }
}

