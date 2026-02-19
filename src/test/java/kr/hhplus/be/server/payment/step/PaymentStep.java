package kr.hhplus.be.server.payment.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PaymentStep {
    private static String PATH_URL = "/payments";

    public static PaymentCommand defaultPaymentCommand() {
        return new PaymentCommand(1L, "tossOrderId", "tossPaymentKey", 50_000L);
    }

    public static Payment defaultPayment() {
        return Payment.builder()
                .amount(50_000L)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Payment paymentWithOrderId(Order order) {
        return Payment.builder()
                .amount(50_000L)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderId(order.getId())
                .build();
    }

    public static PaymentRequest defaultPaymentRequest(){
        return PaymentRequest.builder()
                                .orderId(1L)
                                .tossPaymentKey("tossPaymentKey")
                                .tossOrderId("tossOrderId")
                                .amount(50_000L)
                                .build();
    }

    public static PaymentRequest paymentRequestWithOrderId(long orderId) {
        return PaymentRequest.builder()
                                .orderId(orderId)
                                .amount(50_000L)
                                .tossPaymentKey("tossPaymentKey")
                                .tossOrderId("tossOrderId")
                                .build();
    }

    public static ResultActions paymentRequest(MockMvc mockMvc, ObjectMapper objectMapper, PaymentRequest request) throws Exception {
        return mockMvc.perform(post(PATH_URL)
                                    .content(objectMapper.writeValueAsString(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print());
    }

    public static Payment paymentWithCreatedAt(LocalDateTime localDateTime) {
        return Payment.builder()
                .amount(50_000L)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(localDateTime)
                .build();
    }
}
