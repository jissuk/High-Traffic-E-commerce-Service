package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;

import java.util.Optional;

public interface CouponRepository {
    Optional<CouponEntity> findById(Long couponId);
    CouponEntity save(CouponEntity coupon);



}
