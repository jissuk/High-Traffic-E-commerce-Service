package kr.hhplus.be.server.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import kr.hhplus.be.server.user.domain.model.User;
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
    }

    @Nested
    @DisplayName("결제 실패 케이스")
    class fail{
        @Test
        @DisplayName("존재하지 않는 주문일 경우 OrderNotFoundException이 발생한다.")
        void 결제_존재하지않는_주문일_경우() throws Exception {
            // givne
            long orderId = 2L;
            PaymentRequest request = PaymentStep.paymentRequestWithOrderId(orderId);

            // when
            ResultActions result = PaymentStep.paymentRequest(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("OrderNotFound"));
        }
    }
}
