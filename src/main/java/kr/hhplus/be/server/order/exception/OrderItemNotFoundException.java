package kr.hhplus.be.server.order.exception;

public class OrderItemNotFoundException extends OrderOperationException {
    private static final String DEFAULT_MESSAGE = "주문상세 조회에 실패하였습니다.";

    public OrderItemNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
