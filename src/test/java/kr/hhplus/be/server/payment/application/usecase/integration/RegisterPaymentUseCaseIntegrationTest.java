package kr.hhplus.be.server.payment.application.usecase.integration;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.application.usecase.PaymentRequestUseCase;
import kr.hhplus.be.server.payment.application.usecase.command.PaymentCommand;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("결제 관련 통합 테스트")
public class RegisterPaymentUseCaseIntegrationTest {
    @Autowired private PaymentRequestUseCase registerPaymentUseCase;
    @Autowired private JpaUserRepository userRepository;
    @Autowired private JpaOrderRepository orderRepository;
    @Autowired private JpaOrderItemRepository orderItemRepository;
    @Autowired private JpaPaymentRepository paymentRepository;

    private User savedUser;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
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
            PaymentCommand command = PaymentCommand.builder()
                                                    .orderId(savedOrder.getId())
                                                    .tossOrderId("tossOrderId")
                                                    .tossPaymentKey("tossPaymentKey")
                                                    .amount(50000L)
                                                    .build();
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

    private void clearTestData() {
        userRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        paymentRepository.deleteAll();

        savedUser = null;
        savedOrder = null;
    }

    private void initTestData() {
        User user = User.builder()
                .point(10_000L)
                .build();
        savedUser = userRepository.save(user);

        Order order = Order.builder()
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .totalPrice(50_000L)
                .userId(savedUser.getId())
                .build();
        savedOrder = orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder()
                .orderId(savedOrder.getId())
                .quantity(1L)
                .productId(1L)
                .build();
        orderItemRepository.save(orderItem);

        Payment payment = Payment.builder()
                .amount(50_000L)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderId(savedOrder.getId())
                .build();
        paymentRepository.save(payment);
    }
}

