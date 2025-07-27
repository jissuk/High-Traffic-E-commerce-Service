package kr.hhplus.be.server.user.exception;

//  충전 시 잔고 최대 한도 초과 예외
public class PointLimitExceededException extends UserOperationException {

    private static final String DEFAULT_MESSAGE = "포인트는 최대 100,000포인트까지 충전할 수 있습니다";

    public PointLimitExceededException(){
        super(DEFAULT_MESSAGE);
    }
    public PointLimitExceededException(String message) {
        super(message);
    }
}