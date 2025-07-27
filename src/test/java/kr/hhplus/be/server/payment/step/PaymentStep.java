package kr.hhplus.be.server.payment.step;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;

public class PaymentStep {

    public static PaymentEntity 기본결제엔티티생성(){
        return PaymentEntity.builder()
                        .price(3000L)
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT).build();
    }

    public static PaymentRequestDTO 기본결제요청생성(){
        return PaymentRequestDTO.builder()
                .paymentId(1L)
                .userId(1L)
                .orderId(1L)
                .orderItemId(1L)
                .couponId(1L)
                .productId(1L)
                .build();
    }
}
