package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;

import java.util.Optional;

public interface UserCouponRepository {
    Optional<UserCouponEntity> findById(long couponId);
    Optional<UserCouponEntity> findByCouponId(long id);

    UserCouponEntity save(UserCouponEntity coupon);

}
