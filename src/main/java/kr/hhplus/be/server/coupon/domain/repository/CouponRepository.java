package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.Coupon;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long couponId);
    Coupon save(Coupon coupon);
    Optional<Coupon> findByIdForUpdate(Long couponId);
}
