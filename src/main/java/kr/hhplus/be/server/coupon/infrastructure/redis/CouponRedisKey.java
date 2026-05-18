package kr.hhplus.be.server.coupon.infrastructure.redis;

public class CouponRedisKey {

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";

    public static String getIssuedKey(Long couponId){
        return COUPON_ISSUE_PREFIX + couponId + ISSUED_SUFFIX;
    }

    public static String getQuantityKey(Long couponId){
        return COUPON_ISSUE_PREFIX + couponId + QUANTITY_SUFFIX;
    }
}
