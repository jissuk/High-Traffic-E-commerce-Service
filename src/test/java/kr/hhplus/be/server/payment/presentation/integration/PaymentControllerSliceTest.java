package kr.hhplus.be.server.payment.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.payment.application.usecase.PaymentRequestUseCase;
import kr.hhplus.be.server.payment.presentation.PaymentController;
import kr.hhplus.be.server.payment.presentation.dto.PaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PaymentRequestUseCase paymentRequestUseCase;

    @Test
    @DisplayName("요청 데이터가 정상적일 경우 쿠폰과 포인트를 사용하여 결제한다.")
    void 결제() throws Exception {
        // given
        long orderId = 1L;
        PaymentRequest request = PaymentRequest.builder()
                .orderId(orderId)
                .tossPaymentKey("tossPaymentKey")
                .tossOrderId("tossOrderId")
                .amount(50_000L)
                .build();
        doNothing().when(paymentRequestUseCase).execute(any());

        // when
        ResultActions result = mockMvc.perform(post("/payments/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isNoContent());

        // verify
        verify(paymentRequestUseCase).execute(any());
    }
}
