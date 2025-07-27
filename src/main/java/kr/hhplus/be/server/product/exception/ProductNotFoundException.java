package kr.hhplus.be.server.product.exception;


public class ProductNotFoundException extends ProductOperationException {
    private static final String DEFAULT_MESSAGE = "상품 조회에 실패하였습니다.";

    public ProductNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public ProductNotFoundException(String message) {
        super(message);
    }
}