package kr.hhplus.be.server.payment.usecase.command;

import kr.hhplus.be.server.common.provider.LockKeyProvider;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequest;
import lombok.Builder;

@Builder
public record PaymentCommand(
        Long orderId,
        String tossOrderId,
        String tossPaymentKey,
        Long amount
) implements LockKeyProvider{

    public static PaymentCommand from(PaymentRequest dto) {
        return PaymentCommand.builder()
                                .orderId(dto.orderId())
                                .tossOrderId(dto.tossOrderId())
                                .tossPaymentKey(dto.tossPaymentKey())
                                .amount(dto.amount())
                                .build();
    }

    @Override
    public String lockKey() {
        return "payment:register:" + orderId +":lock";
    }
}
