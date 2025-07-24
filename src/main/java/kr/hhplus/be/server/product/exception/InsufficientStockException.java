package kr.hhplus.be.server.product.exception;

public class InsufficientStockException extends ProductOperationException {

    private static final String DEFAULT_MESSAGE = "상품의 수량이 부족합니다.";

    public InsufficientStockException(){
        super(DEFAULT_MESSAGE);
    }
    public InsufficientStockException(String message) {
        super(message);
    }
}
