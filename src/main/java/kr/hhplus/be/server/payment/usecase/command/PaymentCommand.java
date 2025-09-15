package kr.hhplus.be.server.payment.usecase.command;

import kr.hhplus.be.server.common.provider.LockKeyProvider;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import lombok.Builder;

@Builder
public record PaymentCommand(Long userId, Long orderId, Long orderItemId, Long couponId, Long productId) implements LockKeyProvider{
    public static PaymentCommand from(PaymentRequest dto) {
        return PaymentCommand.builder()
                                .userId(dto.userId())
                                .orderId(dto.orderId())
                                .orderItemId(dto.orderItemId())
                                .couponId(dto.couponId())
                                .productId(dto.productId())
                                .build();
    }

    @Override
    public String lockKey() {
        return "payment:register:" + orderItemId +":lock";
    }
}
