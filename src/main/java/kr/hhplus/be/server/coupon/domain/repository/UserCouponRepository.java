package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;

public interface UserCouponRepository {
    UserCouponEntity findById(long couponId);

    UserCouponEntity save(UserCouponEntity coupon);

    UserCouponEntity findByCouponId(long id);
}
