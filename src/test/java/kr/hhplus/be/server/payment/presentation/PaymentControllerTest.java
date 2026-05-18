package kr.hhplus.be.server.payment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.presentation.dto.PaymentRequest;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("결제 관련 테스트")
public class PaymentControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
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
    @DisplayName("결제 성공 케이스")
    class success {
        @Test
        @DisplayName("요청 데이터가 정상적일 경우 쿠폰과 포인트를 사용하여 결제한다.")
        void 결제() throws Exception {
            // given
            long orderId = savedOrder.getId();
            PaymentRequest request = PaymentRequest.builder()
                                                    .orderId(orderId)
                                                    .tossPaymentKey("tossPaymentKey")
                                                    .tossOrderId("tossOrderId")
                                                    .amount(50_000L)
                                                    .build();

            // when
            ResultActions result = mockMvc.perform(post("/payments/register")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("결제 실패 케이스")
    class fail{
        @Test
        @DisplayName("존재하지 않는 주문일 경우 PaymentNotFoundException이 발생한다.")
        void 결제_존재하지않는_주문일_경우() throws Exception {
            // givne
            long orderId = Integer.MAX_VALUE;
            PaymentRequest request = PaymentRequest.builder()
                                                    .orderId(orderId)
                                                    .amount(50_000L)
                                                    .tossPaymentKey("tossPaymentKey")
                                                    .tossOrderId("tossOrderId")
                                                    .build();

            // when
            ResultActions result = mockMvc.perform(post("/payments/register")
                                            .content(objectMapper.writeValueAsString(request))
                                            .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print());

            // then
            result.andExpect(jsonPath("$.code").value("PaymentNotFound"));
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
