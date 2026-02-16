package kr.hhplus.be.server.coupon.infrastructure.jpa;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaCouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findById(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    Optional<Coupon> findByIdForUpdate(Long couponId);
}
