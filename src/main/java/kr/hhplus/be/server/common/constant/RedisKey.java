package kr.hhplus.be.server.common.constant;

public class RedisKey {

    private RedisKey(){}

    public static class Coupon{

        // =========================
        // 캐싱용 Redis Keys
        // =========================

        public static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";

        public static String userCouponIssuedKey(Long userId, Long couponId) {
            return String.format("coupon:issue:%d:coupon:%d:issued", userId,couponId);
        }
        public static String issueCouponQuantityKey(Long couponId) {
            return String.format("coupon:issue:%d:quantity", couponId);
        }

        // =========================
        // 분산 락용 Redis Keys
        // =========================
        public static final String LOCK_COUPON_ISSUE = "coupon:issue:";
    }

    public static class Product{
        // =========================
        // 캐싱용 Redis Keys
        // =========================

        public static String productSalesKey(String nowDate) {
            return String.format("product:sales:%s", nowDate);
        }

        public static final String PRODUCT_SALES_3DAYS_TOTAL  = "product:sales:3days:total";

        // =========================
        // 분산 락용 Redis Keys
        // =========================
    }

    public static class Payment{
        public static String LOCK_PAYMENT_REGISTER = "payment:register:";
    }
}
