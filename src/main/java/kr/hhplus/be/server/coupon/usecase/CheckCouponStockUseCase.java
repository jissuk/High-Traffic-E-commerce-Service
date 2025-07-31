package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CheckCouponStockUseCase {

    private final CouponRepository couponRepository;

    private final CouponMapper couponMapper;

    public void execute(UserCouponCommand commnad) {

        CouponEntity couponEntity= findCouponOrThrow(commnad.couponId());

        Coupon coupon = couponMapper.toDomain(couponEntity);
        coupon.checkQuantity();
    }

    private CouponEntity findCouponOrThrow(long id) {
        CouponEntity couponEntity = couponRepository.findById(id);
        if (couponEntity == null) {
            throw new CouponNotFoundException();
        }
        return couponEntity;
    }
}
