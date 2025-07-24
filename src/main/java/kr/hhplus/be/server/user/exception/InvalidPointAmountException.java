package kr.hhplus.be.server.user.exception;

// 포인트 충전 시 충전 금액이 0이하 예외
public class InvalidPointAmountException extends UserOperationException {

    private static final String DEFAULT_MESSAGE = "충전하려는 포인트가 유효하지 않은 금액 입니다.";

    public InvalidPointAmountException(){
        super(DEFAULT_MESSAGE);
    }

    public InvalidPointAmountException(String message) {
        super(message);
    }
}