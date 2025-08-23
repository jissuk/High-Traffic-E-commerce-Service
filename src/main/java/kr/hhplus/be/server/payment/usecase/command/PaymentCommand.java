package kr.hhplus.be.server.payment.usecase.command;

import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import lombok.Builder;

@Builder
public record PaymentCommand(Long paymentId, Long userId, Long orderId, Long orderItemId, Long couponId, Long productId) {
    public static PaymentCommand from(PaymentRequest dto) {
        return PaymentCommand.builder()
                                .paymentId(dto.paymentId())
                                .userId(dto.userId())
                                .orderId(dto.orderId())
                                .orderItemId(dto.orderItemId())
                                .couponId(dto.couponId())
                                .productId(dto.productId())
                                .build();
    }
}
