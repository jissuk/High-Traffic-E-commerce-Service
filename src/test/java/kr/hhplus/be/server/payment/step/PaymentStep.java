package kr.hhplus.be.server.payment.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PaymentStep {

    private static String PATH_URL = "/payments";

    public static PaymentCommand 결제커맨드_기본값() {
        return new PaymentCommand(1L, 1L, 1L, 1L, 1L);
    }

    public static Payment 결제_기본값(){
        return Payment.builder()
                .price(3000L)
                .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }

    public static PaymentEntity 결제엔티티_기본값(){
        return PaymentEntity.builder()
                .price(3000L)
                .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                .createAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }

    public static PaymentEntity 결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(long id, LocalDateTime createdAt) {
        return PaymentEntity.builder()
                .price(3000L)
                .paymentStatus(PaymentStatus.COMPLETED)
                .orderItemId(id)
                .createAt(createdAt)
                .build();
    }

    public static PaymentEntity 결제엔티티_기본값(UserEntity user, OrderItemEntity orderItem) {
        return PaymentEntity.builder()
                .price(3000L)
                .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                .createAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .userId(user.getId())
                .orderItemId(orderItem.getId())
                .build();
    }

    public static PaymentRequest 결제요청_기본값(){
        return PaymentRequest.builder()
                                .userId(1L)
                                .orderId(1L)
                                .orderItemId(1L)
                                .couponId(1L)
                                .productId(1L)
                                .build();
    }
    public static PaymentRequest 결제요청_쿠폰ID지정(Long couponId){
        return PaymentRequest.builder()
                                .userId(1L)
                                .orderId(1L)
                                .orderItemId(1L)
                                .couponId(couponId)
                                .productId(1L)
                                .build();
    }

    public static PaymentRequest 결제요청_유저ID지정(long userId){
        return PaymentRequest.builder()
                .userId(userId)
                .orderId(1L)
                .orderItemId(1L)
                .couponId(1L)
                .productId(1L)
                .build();
    }

    public static PaymentRequest 결제요청_주문ID지정(long orderId){
        return PaymentRequest.builder()
                .userId(1L)
                .orderId(orderId)
                .orderItemId(1L)
                .couponId(1L)
                .productId(1L)
                .build();
    }

    public static PaymentRequest 결제요청_주문상세ID지정(long orderItemId){
        return PaymentRequest.builder()
                .userId(1L)
                .orderId(1L)
                .orderItemId(orderItemId)
                .couponId(1L)
                .productId(1L)
                .build();
    }

    public static PaymentRequest 결제요청_결제ID지정(long paymentId){
        return PaymentRequest.builder()
                .userId(1L)
                .orderId(1L)
                .orderItemId(1L)
                .couponId(1L)
                .productId(1L)
                .build();
    }

    public static PaymentRequest 결제요청_상품ID지정(long productId){
        return PaymentRequest.builder()
                .userId(1L)
                .orderId(1L)
                .orderItemId(1L)
                .couponId(1L)
                .productId(productId)
                .build();
    }

    public static ResultActions 결제요청(MockMvc mockMvc, ObjectMapper objectMapper, PaymentRequest request) throws Exception {

        return mockMvc.perform(post(PATH_URL)
                                    .content(objectMapper.writeValueAsString(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print());

    }

}
