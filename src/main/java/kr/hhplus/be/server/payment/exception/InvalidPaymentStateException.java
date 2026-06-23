package kr.hhplus.be.server.payment.exception;

import kr.hhplus.be.server.payment.domain.model.PaymentAction;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;

public class InvalidPaymentStateException extends PaymentOperationException {
    public InvalidPaymentStateException(PaymentStatus currentStatus, PaymentAction action) {
        super(buildMessage(currentStatus, action));
    }

    private static String buildMessage(PaymentStatus currentStatus, PaymentAction action) {
        return String.format(
                "Invalid payment state transition. currentStatus=%s, action=%s",
                currentStatus,
                action
        );
    }
}
