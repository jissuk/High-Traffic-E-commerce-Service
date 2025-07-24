package kr.hhplus.be.server.product.exception;

public abstract class ProductOperationException extends RuntimeException {
    public ProductOperationException(String message) {
        super(message);
    }
}