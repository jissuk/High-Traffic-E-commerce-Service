package kr.hhplus.be.server.coupon.usecase.reader;

import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponReader {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public Coupon findCouponOrThrow(long id){
        CouponEntity couponEntity = couponRepository.findById(id).orElseThrow(CouponNotFoundException::new);
        return couponMapper.toDomain(couponEntity);
    }
}
