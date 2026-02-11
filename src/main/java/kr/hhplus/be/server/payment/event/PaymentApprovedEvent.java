package kr.hhplus.be.server.payment.event;

import kr.hhplus.be.server.payment.domain.model.Payment;
import lombok.Builder;

@Builder
public record PaymentApprovedEvent(
    long paymentId,
    long amount,
    String tossPaymentKey,
    String tossOrderId
) {
    public static PaymentApprovedEvent of(Payment payment){
        return PaymentApprovedEvent.builder()
                                    .paymentId(payment.getId())
                                    .amount(payment.getAmount())
                                    .tossOrderId(payment.getTossOrderId())
                                    .tossPaymentKey(payment.getTossPaymentKey())
                                    .build();
    }
}
