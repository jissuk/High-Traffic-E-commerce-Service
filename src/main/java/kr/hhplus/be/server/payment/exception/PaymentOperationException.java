package kr.hhplus.be.server.payment.exception;

public abstract class PaymentOperationException extends RuntimeException {
    public PaymentOperationException(String message) {
        super(message);
    }
}