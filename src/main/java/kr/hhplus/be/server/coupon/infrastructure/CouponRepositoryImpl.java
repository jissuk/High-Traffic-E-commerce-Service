package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponMapper couponMapper;

    private final JpaCouponRepository jpaCouponRepository;


    @Override
    public Coupon findById(Long couponId) {

        return jpaCouponRepository.findById(couponId)
                .map(couponMapper::toDomain)
                .orElseThrow(CouponNotFoundException::new);
    }

    @Override
    public Coupon findByIdForUpdate(Long couponId) {
        return jpaCouponRepository.findByIdForUpdate(couponId)
                .map(couponMapper::toDomain)
                .orElseThrow(CouponNotFoundException::new);
    }
    @Override
    public Coupon save(Coupon coupon) {
        return couponMapper.toDomain(
                jpaCouponRepository.save(couponMapper.toEntity(coupon))
        );
    }

}
