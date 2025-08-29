package kr.hhplus.be.server.coupon.event;

public record IssueCouponEvent(
    long userId,
    long couponId
) {

}
