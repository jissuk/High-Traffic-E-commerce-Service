package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;

import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon findById(long couponId);
    UserCoupon findByCouponId(long id);
    UserCoupon save(UserCoupon coupon);

}
