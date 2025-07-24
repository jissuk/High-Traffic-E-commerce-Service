package kr.hhplus.be.server.user.exception;

public class UserNotFoundException extends UserOperationException {

    private static final String DEFAULT_MESSAGE = "유저 조회에 실패하였습니다.";

    public UserNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
