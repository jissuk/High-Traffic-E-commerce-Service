package kr.hhplus.be.server.coupon.event;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import lombok.Builder;

@Builder
public record IssueCouponEvent(
    long userId,
    long couponId
) {
    public static IssueCouponEvent of(UserCoupon userCoupon) {
        return IssueCouponEvent.builder()
                .couponId(userCoupon.getCouponId())
                .userId(userCoupon.getUserId())
                .build();
    }
}
