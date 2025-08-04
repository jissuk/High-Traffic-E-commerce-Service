package kr.hhplus.be.server.coupon.infrastructure.jpa;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Integer> {
    Optional<CouponEntity> findById(long id);
}
