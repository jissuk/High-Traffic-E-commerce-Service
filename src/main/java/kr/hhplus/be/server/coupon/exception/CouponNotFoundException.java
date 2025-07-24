package kr.hhplus.be.server.coupon.exception;

import kr.hhplus.be.server.order.exception.OrderOperationException;

public class CouponNotFoundException extends OrderOperationException {

    private static final String DEFAULT_MESSAGE = "쿠폰 조회에 실패하였습니다.";

    public CouponNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public CouponNotFoundException(String message) {
        super(message);
    }
}