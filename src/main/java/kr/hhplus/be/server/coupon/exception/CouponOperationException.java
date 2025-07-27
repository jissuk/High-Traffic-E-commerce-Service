package kr.hhplus.be.server.coupon.exception;

public abstract class CouponOperationException extends RuntimeException {
    public CouponOperationException(String message) {
        super(message);
    }
}