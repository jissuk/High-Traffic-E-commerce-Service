package kr.hhplus.be.server.order.exception;

public abstract class OrderOperationException extends RuntimeException {
    public OrderOperationException(String message) {
        super(message);
    }
}