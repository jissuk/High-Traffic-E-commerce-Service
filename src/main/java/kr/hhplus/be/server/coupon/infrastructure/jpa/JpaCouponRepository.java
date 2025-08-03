package kr.hhplus.be.server.coupon.infrastructure.jpa;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Integer> {
    CouponEntity findById(long id);
}
