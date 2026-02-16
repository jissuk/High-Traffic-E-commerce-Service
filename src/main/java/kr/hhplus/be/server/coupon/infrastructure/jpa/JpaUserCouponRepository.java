package kr.hhplus.be.server.coupon.infrastructure.jpa;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Integer> {
    Optional<UserCoupon> findById(long id);
    Optional<UserCoupon> findByCouponId(long couponId);
}
