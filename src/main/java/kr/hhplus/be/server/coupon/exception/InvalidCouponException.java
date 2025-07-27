package kr.hhplus.be.server.coupon.exception;

public class InvalidCouponException extends CouponOperationException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 쿠폰입니다.";

    public InvalidCouponException(){
        super(DEFAULT_MESSAGE);
    }
    public InvalidCouponException(String message) {
        super(message);
    }
}
