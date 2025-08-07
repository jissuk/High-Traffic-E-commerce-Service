package kr.hhplus.be.server.coupon.infrastructure.jpa;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Integer> {
    Optional<CouponEntity> findById(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CouponEntity c WHERE c.id = :couponId")
    Optional<CouponEntity> findByIdForUpdate(Long couponId);
}
