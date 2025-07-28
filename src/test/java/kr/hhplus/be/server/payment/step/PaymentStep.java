package kr.hhplus.be.server.payment.step;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.user.usecase.command.UserCommand;

public class PaymentStep {

    public static PaymentCommand 결제커맨드_기본값() {
        return new PaymentCommand(1L, 1L, 1L, 1L, 1L, 1L);
    }

    public static PaymentEntity 결제엔티티_기본값(){
        return PaymentEntity.builder()
                        .price(3000L)
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT).build();
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


}
