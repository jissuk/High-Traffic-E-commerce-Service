package kr.hhplus.be.server.payment.step;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PaymentStep {

    private static String PATH_URL = "/payments";

    public static PaymentCommand 결제커맨드_기본값() {
        return new PaymentCommand(1L, 1L, 1L, 1L, 1L, 1L);
    }

    public static PaymentEntity 결제엔티티_기본값(){
        return PaymentEntity.builder()
                        .price(3000L)
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT).build();
    }

    public static PaymentEntity 결제엔티티_기본값(UserEntity user){
        return PaymentEntity.builder()
                .price(3000L)
                .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                .user(user)
                .build();
    }

    public static PaymentRequestDTO 결제요청_기본값(){
        return PaymentRequestDTO.builder()
                                .paymentId(1L)
                                .userId(1L)
                                .orderId(1L)
                                .orderItemId(1L)
                                .couponId(1L)
                                .productId(1L)
                                .build();
    }



    public static ResultActions 결제요청(MockMvc mockMvc, ObjectMapper objectMapper, PaymentRequestDTO request) throws Exception {

        return mockMvc.perform(post(PATH_URL)
                                    .content(objectMapper.writeValueAsString(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print());

    }

}
