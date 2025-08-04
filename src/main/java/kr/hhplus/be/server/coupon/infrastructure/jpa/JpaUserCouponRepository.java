package kr.hhplus.be.server.coupon.infrastructure.jpa;

import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserCouponRepository extends JpaRepository<UserCouponEntity, Integer> {
    Optional<UserCouponEntity> findById(long id);

    Optional<UserCouponEntity> findByCouponId(long couponId);
}
