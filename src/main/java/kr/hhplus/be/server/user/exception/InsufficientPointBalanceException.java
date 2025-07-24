package kr.hhplus.be.server.user.exception;

// 포인트 사용시 상품 금액이 잔고 초과 예외
public class InsufficientPointBalanceException extends UserOperationException {

    private static final String DEFAULT_MESSAGE = "포인트가 부족하여 요청하신 결제를 진행할 수 없습니다.";

    public InsufficientPointBalanceException(){
        super(DEFAULT_MESSAGE);
    }

    public InsufficientPointBalanceException(String message) {
        super(message);
    }
}
