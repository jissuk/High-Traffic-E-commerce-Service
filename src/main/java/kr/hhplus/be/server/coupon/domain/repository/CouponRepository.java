package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;

public interface CouponRepository {
    CouponEntity save(CouponEntity coupon);

    CouponEntity findById(Long couponId);

    void update(CouponEntity updateCoupon);

    void clear();

}
