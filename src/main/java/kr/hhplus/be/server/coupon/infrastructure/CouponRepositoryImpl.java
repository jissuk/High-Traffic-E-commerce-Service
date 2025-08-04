package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final JpaCouponRepository jpaCouponRepository;

    public CouponRepositoryImpl(JpaCouponRepository jpaCouponRepository) {
        this.jpaCouponRepository = jpaCouponRepository;
    }

    @Override
    public Optional<CouponEntity> findById(Long couponId) {

        return jpaCouponRepository.findById(couponId);
    }

    @Override
    public CouponEntity save(CouponEntity coupon) {
        return jpaCouponRepository.save(coupon);
    }
}
