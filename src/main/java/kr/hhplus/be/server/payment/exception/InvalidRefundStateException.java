package kr.hhplus.be.server.payment.exception;

import kr.hhplus.be.server.payment.domain.model.RefundAction;
import kr.hhplus.be.server.payment.domain.model.RefundStatus;
import lombok.Getter;

@Getter
public class InvalidRefundStateException extends PaymentOperationException {

    private final RefundStatus currentStatus;
    private final RefundAction action;

    public InvalidRefundStateException(RefundStatus currentStatus, RefundAction action) {
        super(buildMessage(currentStatus, action));
        this.currentStatus = currentStatus;
        this.action = action;
    }

    private static String buildMessage(RefundStatus currentStatus, RefundAction action) {
        return String.format(
                "Invalid refund state transition. currentStatus=%s, action=%s",
                currentStatus,
                action
        );
    }
}