package kr.hhplus.be.server.order.exception;

import kr.hhplus.be.server.order.domain.model.OrderAction;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import lombok.Getter;

@Getter
public class InvalidOrderStateException extends OrderOperationException {

    private final OrderStatus currentStatus;
    private final OrderAction action;

    public InvalidOrderStateException(OrderStatus currentStatus, OrderAction action) {
        super(buildMessage(currentStatus, action));
        this.currentStatus = currentStatus;
        this.action = action;
    }

    private static String buildMessage(OrderStatus currentStatus, OrderAction action) {
        return String.format(
                "Invalid order state transition. currentStatus=%s, action=%s",
                currentStatus,
                action
        );
    }
}