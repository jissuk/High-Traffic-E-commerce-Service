package kr.hhplus.be.server.order.exception;

public class OrderNotFoundException extends OrderOperationException {

    private static final String DEFAULT_MESSAGE = "주문 조회에 실패하였습니다.";

    public OrderNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public OrderNotFoundException(String message) {
        super(message);
    }
}