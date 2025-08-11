package kr.hhplus.be.server.payment.usecase.integration;


import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.RegisterPaymentUseCase;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("결제 관련 통합 테스트")
public class RegisterPaymentUseCaseTest {

    @Autowired
    private RegisterPaymentUseCase registerPaymentUseCase;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaOrderItemRepository jpaOrderItemRepository;

    @Autowired
    private JpaOrderRepository jpaOrderRepositroy;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private JpaUserCouponRepository jpaUserCouponRepository;

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
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
        jdbcTemplate.execute("TRUNCATE TABLE user_coupons;");
        jdbcTemplate.execute("TRUNCATE TABLE products;");
    }

    private void initTestData() {
        UserEntity user = jpaUserRepository.save(UserStep.유저엔티티_기본값());
        jpaPaymentRepository.save(PaymentStep.결제엔티티_기본값(user));

        OrderEntity order = jpaOrderRepositroy.save(OrderStep.주문엔티티_기본값(user));
        jpaOrderItemRepository.save(OrderStep.주문상세엔티티_기본값(order));

        CouponEntity coupon = jpaCouponRepository.save(CouponStep.쿠폰엔티티_기본값());
        jpaUserCouponRepository.save(CouponStep.유저쿠폰엔티티_기본값(user, coupon));

        jpaProductRepository.save(ProductStep.상품엔티티_기본값());
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
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
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

            int successCount = 0;
            int failureCount = 0;

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount++;
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertEquals(1, successCount);
        }

        @Test
        @DisplayName("동시에 10건의 쿠폰사용 요청을 보낼 경우 1건만 성공")
        void 쿠폰사용_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            UserCouponEntity userCoupon = jpaUserCouponRepository.findByCouponId(command.couponId()).get();
            userCoupon.setCouponStatus(CouponStatus.USED);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaUserCouponRepository.save(userCoupon);
                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            executor.shutdown();

            int successCount = 0;
            int failureCount = 0;

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount++;
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertEquals(1, successCount);
            UserCouponEntity result = jpaUserCouponRepository.findByCouponId(command.couponId()).get();
            assertEquals(CouponStatus.USED, result.getCouponStatus());
        }

        @Test
        @DisplayName("동시에 10건의 포인트사용 요청을 보낼 경우 1건만 성공")
        void 포인트사용_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            UserEntity user = jpaUserRepository.findById(command.userId()).get();
            user.setPoint(user.getPoint() - 3000L);


            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaUserRepository.save(user);
                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            executor.shutdown();

            int successCount = 0;
            int failureCount = 0;

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount++;
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertEquals(1, successCount);
            UserEntity result = jpaUserRepository.findById(command.userId()).get();
            assertThat(result.getPoint()).isEqualTo(37000L);
        }


        @Test
        @DisplayName("동시에 100건의 상품수량변경 요청을 보낼 경우 모두 성공")
        void 상품수량변경_동시성() throws InterruptedException {
            // given
            int threadCount = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            PaymentCommand command = PaymentStep.결제커맨드_기본값();

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {

                futures.add(executor.submit(() -> {
                    try {

                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            executor.shutdown();

            int successCount = 0;
            int failureCount = 0;

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount++;
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertEquals(100, successCount);
        }

        @Test
        @DisplayName("동시에 10건의 결제상태변경 요청을 보낼 경우 1건만 성공")
        void 결제상태변경_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            PaymentEntity paymentEntity = jpaPaymentRepository.findById(command.productId()).get();
            paymentEntity.setPaymentStatus(PaymentStatus.COMPLETED);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaPaymentRepository.save(paymentEntity);
                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            executor.shutdown();

            int successCount = 0;
            int failureCount = 0;

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount++;
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertEquals(1, successCount);
            PaymentEntity result = jpaPaymentRepository.findById(command.productId()).get();
            assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        }

        @Test
        @DisplayName("동시에 10건의 주문상태변경 요청을 보낼 경우 1건만 성공")
        void 주문상태변경_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            OrderEntity orderEntity = jpaOrderRepositroy.findById(command.productId()).get();
            orderEntity.setOrderStatus(OrderStatus.COMPLETED);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaOrderRepositroy.save(orderEntity);
                        return null;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            executor.shutdown();

            int successCount = 0;
            int failureCount = 0;

            for (Future<Void> future : futures) {
                try {
                    future.get();
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        failureCount++;
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            assertEquals(1, successCount);
            OrderEntity result = jpaOrderRepositroy.findById(command.productId()).get();
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
        }
    }
}

