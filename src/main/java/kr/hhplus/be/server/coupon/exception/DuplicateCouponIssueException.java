package kr.hhplus.be.server.coupon.exception;

import kr.hhplus.be.server.user.exception.UserOperationException;

public class DuplicateCouponIssueException extends UserOperationException {

    private static final String DEFAULT_MESSAGE = "해당 쿠폰은 중복으로 발급할 수 없습니다.";

    public DuplicateCouponIssueException(){
        super(DEFAULT_MESSAGE);
    }
    public DuplicateCouponIssueException(String message) {
        super(message);
    }
}
