package kr.hhplus.be.server.payment.event;

import kr.hhplus.be.server.payment.domain.model.Payment;
import lombok.Builder;

@Builder
public record PaymentRequestEvent(
        long paymentId,
        long amount,
        String tossPaymentKey,
        String tossOrderId
){
    public static PaymentRequestEvent of(Payment payment){
        return PaymentRequestEvent.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .tossOrderId(payment.getTossOrderId())
                .tossPaymentKey(payment.getTossPaymentKey())
                .build();
    }
}
