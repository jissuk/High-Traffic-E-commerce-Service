package kr.hhplus.be.server.payment.usecase.command;

import kr.hhplus.be.server.payment.usecase.dto.PaymentConfirmRequest;
import lombok.Builder;

@Builder
public record PaymentConfirmCommand(Long paymentId, String orderId, String paymentKey, Long amount) {
    public static PaymentConfirmCommand from(PaymentConfirmRequest request) {
        return  PaymentConfirmCommand.builder()
                                        .paymentId(request.paymentId())
                                        .orderId(request.orderId())
                                        .paymentKey(request.paymentKey())
                                        .amount(request.amount())
                                        .build();
    }
}
