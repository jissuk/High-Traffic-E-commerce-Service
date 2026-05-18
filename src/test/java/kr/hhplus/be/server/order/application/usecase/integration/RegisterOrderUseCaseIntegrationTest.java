package kr.hhplus.be.server.order.application.usecase.integration;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.application.usecase.RegisterOrderUseCase;
import kr.hhplus.be.server.order.application.usecase.command.OrderCommand;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;

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
            long productId = savedProduct.getId();
            long userId = savedUser.getId();
            long userCouponId = savedUserCoupon.getId();
            long quantity = 2L;
            long point = 3000L;
            OrderCommand request = OrderCommand.builder()
                                                .productId(productId)
                                                .userId(userId)
                                                .userCouponId(userCouponId)
                                                .quantity(quantity)
                                                .point(point)
                                                .build();

            // when
            registerOrderUseCase.execute(request);

            // then
            long createdId = 1L;
            assertAll(
                ()-> assertThat(productRepository.findById(createdId).get().getQuantity())
                        .as("남은 상품 수량 확인")
                        .isEqualTo(3L),
                ()-> assertThat(orderRepository.findById(createdId).get().getId())
                        .as("주문 생성 확인")
                        .isEqualTo(createdId),
                ()-> assertThat(orderItemRepository.findById(createdId).get().getId())
                        .as("주문 상세 생성 확인")
                        .isEqualTo(createdId),
                ()-> assertThat(paymentRepository.findById(createdId).get().getId())
                        .as("결제 생성 확인")
                        .isEqualTo(createdId)
            );
        }
    }

    private void initTestDBData(){
        User user = User.builder()
                        .point(10_000L)
                        .build();
        savedUser = userRepository.save(user);

        long couponDiscount = 3000L;
        long couponQuantity = 500L;
        String couponDescription = "여름 특별 할인 쿠폰";
        LocalDateTime expiration = LocalDateTime.now().plusMonths(3);
        Coupon coupon = Coupon.builder()
                                .discount(couponDiscount)
                                .quantity(couponQuantity)
                                .description(couponDescription)
                                .expiredAt(expiration)
                                .build();
        savedCoupon = couponRepository.save(coupon);

        UserCoupon userCoupon = UserCoupon.builder()
                                            .couponStatus(CouponStatus.ISSUED)
                                            .userId(savedUser.getId())
                                            .couponId(savedCoupon.getId())
                                            .build();
        savedUserCoupon = userCouponRepository.save(userCoupon);

        Product product = Product.builder()
                .productName("기본 상품")
                .price(2000L)
                .quantity(5L)
                .build();
        savedProduct = productRepository.save(product);
    }
}
