package kr.hhplus.be.server.coupon.usecase.reader;

import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponReader {

    private final UserCouponRepository userCouponRepositor;
    private final UserCouponMapper userCouponMapper;

    public UserCoupon findUserCouponOrThrow(long id){
        UserCouponEntity userCouponEntity = userCouponRepositor.findById(id).orElseThrow(UserCouponNotFoundException::new);
        return userCouponMapper.toDomain(userCouponEntity);
    }

    public UserCoupon findByCouponIdUserCouponOrThrow(long couponId){
        UserCouponEntity userCouponEntity = userCouponRepositor.findByCouponId(couponId).orElseThrow(UserCouponNotFoundException::new);
        return userCouponMapper.toDomain(userCouponEntity);
    }

}
