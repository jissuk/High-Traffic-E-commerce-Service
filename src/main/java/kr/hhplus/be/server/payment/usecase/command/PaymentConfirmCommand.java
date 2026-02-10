package kr.hhplus.be.server.payment.usecase.command;

import kr.hhplus.be.server.payment.usecase.dto.PaymentConfirmRequest;
import lombok.Builder;

@Builder
public record PaymentConfirmCommand(String orderId, String paymentKey, Long amount) {
    public static PaymentConfirmCommand from(PaymentConfirmRequest request) {
        return  PaymentConfirmCommand.builder()
                                        .orderId(request.orderId())
                                        .paymentKey(request.paymentKey())
                                        .amount(request.amount())
                                        .build();
    }
}
