package kr.hhplus.be.server.coupon.infrastructure.jpa;

import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserCouponRepository extends JpaRepository<UserCouponEntity, Integer> {
    UserCouponEntity findById(long id);

    UserCouponEntity findByCouponId(long couponId);
}
