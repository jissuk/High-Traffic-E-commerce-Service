package kr.hhplus.be.server.coupon.exception;

import kr.hhplus.be.server.user.exception.UserOperationException;

public class UserCouponNotFoundException extends UserOperationException {

    private static final String DEFAULT_MESSAGE = "유저 쿠폰 조회에 실패하였습니다.";

    public UserCouponNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public UserCouponNotFoundException(String message) {
        super(message);
    }
}
