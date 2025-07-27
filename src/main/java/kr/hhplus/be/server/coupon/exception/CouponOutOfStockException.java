package kr.hhplus.be.server.coupon.exception;

public class CouponOutOfStockException  extends CouponOperationException {

    private static final String DEFAULT_MESSAGE = "쿠폰 수량이 모두 소진되었습니다.";

    public CouponOutOfStockException (){
        super(DEFAULT_MESSAGE);
    }
    public CouponOutOfStockException (String message) {
        super(message);
    }
}
