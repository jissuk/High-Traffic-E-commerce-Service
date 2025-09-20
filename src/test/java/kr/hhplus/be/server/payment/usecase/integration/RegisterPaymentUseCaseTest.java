package kr.hhplus.be.server.payment.usecase.integration;


import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.RegisterPaymentUseCase;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.User;
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
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private OrderMapper orderMapper;


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
        OrderEntity order = jpaOrderRepositroy.save(OrderStep.defaultOrderEntity(user));
        CouponEntity coupon = jpaCouponRepository.save(CouponStep.defaultCouponEntity());
        OrderItemEntity orderItem = jpaOrderItemRepository.save(OrderStep.defaultOrderItemEntity(order));

        jpaPaymentRepository.save(PaymentStep.defaultPaymentEntity(user,orderItem));
        jpaUserCouponRepository.save(CouponStep.defaultUserCouponEntity(user, coupon));
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

        @Test
        @DisplayName("동시에 10건의 쿠폰사용 요청을 보낼 경우 1건만 성공")
        void 쿠폰사용_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.defaultPaymentCommand();
            UserCouponEntity userCouponEntity = jpaUserCouponRepository.findByCouponId(command.couponId()).get();

            UserCoupon userCoupon = userCouponMapper.toDomain(userCouponEntity);
            userCoupon.useCoupon();

            UserCouponEntity updateUserCoupon = userCouponMapper.toEntity(userCoupon);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaUserCouponRepository.save(updateUserCoupon);
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
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            UserCouponEntity result = jpaUserCouponRepository.findByCouponId(command.couponId()).get();
            assertAll(
                ()-> assertThat(successCount.get())
                        .as("성공한 요청 수")
                        .isEqualTo(1L),
                ()-> assertThat(failureCount.get())
                        .as("실패한 요청 수")
                        .isEqualTo(9L),
                ()-> assertThat(result.getCouponStatus())
                        .as("쿠폰 사용 상태 확인")
                        .isEqualTo(CouponStatus.USED)
            );
        }

        @Test
        @DisplayName("동시에 10건의 포인트사용 요청을 보낼 경우 1건만 성공")
        void 포인트사용_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.defaultPaymentCommand();
            UserEntity userEntity = jpaUserRepository.findById(command.userId()).get();

            User user = userMapper.toDomain(userEntity);
            user.deductPoint(3000L);

            UserEntity updateUser = userMapper.toEntity(user);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaUserRepository.save(updateUser);
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
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            UserEntity result = jpaUserRepository.findById(command.userId()).get();
            assertAll(
                ()-> assertThat(successCount.get())
                        .as("성공한 요청 수")
                        .isEqualTo(1L),
                ()-> assertThat(failureCount.get())
                        .as("실패한 요청 수")
                        .isEqualTo(9L),
                ()-> assertThat(result.getPoint())
                        .as("차감된 포인트 확인")
                        .isEqualTo(37000L)
            );
        }


        @Test
        @DisplayName("동시에 100건의 상품수량변경 요청을 보낼 경우 모두 성공")
        void 상품수량변경_분산락_동시성() throws InterruptedException {
            // given
            int threadCount = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            PaymentCommand command = PaymentStep.defaultPaymentCommand();

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {

                futures.add(executor.submit(() -> {
                    try {
                        //
                        ProductEntity productEntity = jpaProductRepository.findById(command.productId()).get();

                        Product product = productMapper.toDomain(productEntity);
                        product.deductQuantity(1);

                        ProductEntity updateProduct = productMapper.toEntity(product);
                        jpaProductRepository.save(updateProduct);
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
                        .isEqualTo(100L),
                ()-> assertThat(failureCount.get())
                        .as("실패한 요청 수")
                        .isEqualTo(0L)
            );
        }

        @Test
        @DisplayName("동시에 10건의 결제상태변경 요청을 보낼 경우 1건만 성공")
        void 결제상태변경_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.defaultPaymentCommand();
            PaymentEntity paymentEntity = jpaPaymentRepository.findById(command.productId()).get();

            Payment payment = paymentMapper.toDomain(paymentEntity);
            payment.complete();

            PaymentEntity updatePayment = paymentMapper.toEntity(payment);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaPaymentRepository.save(updatePayment);
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
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            PaymentEntity result = jpaPaymentRepository.findById(command.productId()).get();
            assertAll(
                ()-> assertThat(successCount.get())
                        .as("성공한 요청 수")
                        .isEqualTo(1L),
                ()-> assertThat(failureCount.get())
                        .as("실패한 요청 수")
                        .isEqualTo(9L),
                ()-> assertThat(result.getPaymentStatus())
                        .as("결제 상태 변경 확인")
                        .isEqualTo(PaymentStatus.COMPLETED)
            );
        }

        @Test
        @DisplayName("동시에 10건의 주문상태변경 요청을 보낼 경우 1건만 성공")
        void 주문상태변경_동시성() throws InterruptedException {
            // given
            PaymentCommand command = PaymentStep.defaultPaymentCommand();
            OrderEntity orderEntity = jpaOrderRepositroy.findById(command.productId()).get();

            Order order = orderMapper.toDomain(orderEntity);
            order.complete();

            OrderEntity updateOrder = orderMapper.toEntity(order);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        jpaOrderRepositroy.save(updateOrder);
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
                    } else{
                        e.getCause().printStackTrace();
                    }
                }
            }

            System.out.println("성공한 요청 수: " + successCount);
            System.out.println("실패한 요청 수: " + failureCount);

            // then
            OrderEntity result = jpaOrderRepositroy.findById(command.productId()).get();
            assertAll(
                    ()-> assertThat(successCount.get())
                            .as("성공한 요청 수")
                            .isEqualTo(1L),
                    ()-> assertThat(failureCount.get())
                            .as("실패한 요청 수")
                            .isEqualTo(9L),
                ()-> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED)
            );
        }
    }
}

