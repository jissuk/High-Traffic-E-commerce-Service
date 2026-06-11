package kr.hhplus.be.server.order.application.usecase.integration;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.fixture.CouponFixture;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.application.usecase.RegisterOrderUseCase;
import kr.hhplus.be.server.order.application.usecase.command.OrderCommand;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.fixture.ProductFixture;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.fixture.UserFixture;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@DisplayName("주문 관련 테스트")
@Import(TestcontainersConfiguration.class)
public class RegisterOrderUseCaseIntegrationTest {
    @Autowired private RegisterOrderUseCase registerOrderUseCase;
    @Autowired private JpaUserRepository userRepository;
    @Autowired private JpaCouponRepository couponRepository;
    @Autowired private JpaUserCouponRepository userCouponRepository;
    @Autowired private JpaProductRepository productRepository;
    @Autowired private JpaPaymentRepository paymentRepository;
    @Autowired private JpaOrderRepository orderRepository;
    @Autowired private JpaOrderItemRepository orderItemRepository;

    private User savedUser;
    private Coupon savedCoupon;
    private UserCoupon savedUserCoupon;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        initTestDBData();
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 주문() {
            // given
            OrderCommand command = createOrderCommand();
            long remainingQuantity = 3L;

            // when
            registerOrderUseCase.execute(command);

            // then
            Product product = productRepository.findById(savedProduct.getId()).orElseThrow();
            assertAll(
                    () -> assertThat(product.getQuantity())
                            .isEqualTo(remainingQuantity),

                    () -> assertThat(orderRepository.count())
                            .isEqualTo(1),

                    () -> assertThat(orderItemRepository.count())
                            .isEqualTo(1),

                    () -> assertThat(paymentRepository.count())
                            .isEqualTo(1)
            );
        }
    }

    private void initTestDBData(){
//        Product product = ProductFixture.create(); // quantity : 5
        Product product = ProductFixture.builder().build(); // quantity : 5
        savedProduct = productRepository.save(product);

//        User user = UserFixture.create();
        User user = UserFixture.builder().build();
        savedUser = userRepository.save(user);

        Coupon coupon = CouponFixture.builder().build();
        savedCoupon = couponRepository.save(coupon);

        UserCoupon userCoupon = UserCoupon.builder()
                                            .couponStatus(CouponStatus.ISSUED)
                                            .userId(savedUser.getId())
                                            .couponId(savedCoupon.getId())
                                            .build();
        savedUserCoupon = userCouponRepository.save(userCoupon);
    }

    private OrderCommand createOrderCommand(){
        long productId = savedProduct.getId();
        long userId = savedUser.getId();
        long userCouponId = savedUserCoupon.getId();
        long quantity = 2L;
        long point = 3000L;
        return OrderCommand.builder()
                .productId(productId)
                .userId(userId)
                .userCouponId(userCouponId)
                .quantity(quantity)
                .point(point)
                .build();
    }
}
