package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.order.exception.InvalidOrderStateException;

public enum OrderStatus {

    CREATED {
        @Override
        public OrderStatus complete() {
            return COMPLETED;
        }

        @Override
        public OrderStatus cancel() {
            return CANCELLED;
        }
    },

    COMPLETED,

    CANCELLED;

    public OrderStatus complete() {
        throw new InvalidOrderStateException(this, OrderAction.COMPLETE);
    }

    public OrderStatus cancel() {
        throw new InvalidOrderStateException(this, OrderAction.CANCEL);
    }
}