package kr.hhplus.be.server.common.outbox.exception;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxAction;
import kr.hhplus.be.server.common.outbox.domain.model.OutboxStatus;
import lombok.Getter;

@Getter
public class InvalidOutboxStateException extends RuntimeException {

    private final OutboxStatus status;
    private final OutboxAction action;

    public InvalidOutboxStateException(OutboxStatus currentStatus, OutboxAction action) {
        super(buildMessage(currentStatus, action));
        this.status = currentStatus;
        this.action = action;
    }

    private static String buildMessage(OutboxStatus currentStatus, OutboxAction action) {
        return String.format(
                "Invalid outbox state transition. currentStatus=%s, action=%s",
                currentStatus,
                action
        );
    }
}