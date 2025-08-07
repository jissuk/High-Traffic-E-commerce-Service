package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;

import java.util.Optional;

public interface UserCouponRepository {
    Optional<UserCoupon> findById(long couponId);
    Optional<UserCoupon> findByCouponId(long id);
    UserCoupon save(UserCoupon coupon);

}
