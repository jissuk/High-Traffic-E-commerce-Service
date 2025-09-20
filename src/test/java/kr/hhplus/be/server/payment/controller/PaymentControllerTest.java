package kr.hhplus.be.server.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("결제 관련 테스트")
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        UserEntity user = jpaUserRepository.save(UserStep.defualtUserEntity());
        CouponEntity coupon = jpaCouponRepository.save(CouponStep.defaultCouponEntity());
        OrderEntity order = jpaOrderRepositroy.save(OrderStep.defaultOrderEntity(user));
        OrderItemEntity orderItem = jpaOrderItemRepository.save(OrderStep.defaultOrderItemEntity(order));

        jpaUserCouponRepository.save(CouponStep.defaultUserCouponEntity(user, coupon));
        jpaProductRepository.save(ProductStep.defaultProductEntity());
        jpaPaymentRepository.save(PaymentStep.defaultPaymentEntity(user,orderItem));
    }

    @Nested
    @DisplayName("결제 성공 케이스")
    class success {

        @Test
        @DisplayName("요청 데이터가 정상적일 경우 쿠폰과 포인트를 사용하여 결제한다.")
        void 결제() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.defaultPaymentRequest();

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("요청 데이터가 정상적일 경우 포인트만 사용하여 결제한다.")
        void 결제_쿠폰이존재하지않을_경우() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.paymentRequestWithCouponId(null);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("결제 실패 케이스")
    class fail{
        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 결제_존재하지않는_유저일_경우() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.paymentRequestWithUserId(2L);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("UserNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 주문일 경우 OrderNotFoundException이 발생한다.")
        void 결제_존재하지않는_주문일_경우() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.paymentRequestWithOrderId(2L);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("OrderNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 주문상세일 경우 OrderItemNotFoundException이 발생한다.")
        void 결제_존재하지않는_주문상세일_경우() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.paymentRequestWithOrderItemId(2L);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("OrderItemNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 결제일 경우 PaymentNotFoundException이 발생한다.")
        void 결제_존재하지않는_결제일_경우() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.paymentRequestWithPaymentId(2L);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("PaymentNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 결제_존재하지않는_상품일_경우() throws Exception {
            // givne
            PaymentRequest request = PaymentStep.paymentRequestWithProductId(2L);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("ProductNotFound"));
        }
    }
}
