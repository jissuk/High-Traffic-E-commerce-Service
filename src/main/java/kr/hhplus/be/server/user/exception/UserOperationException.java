package kr.hhplus.be.server.user.exception;

public abstract class UserOperationException extends RuntimeException {
    public UserOperationException(String message) {
        super(message);
    }
}