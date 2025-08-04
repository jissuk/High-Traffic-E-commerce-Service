package kr.hhplus.be.server.coupon.domain.mapper;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import org.springframework.stereotype.Component;

@Component
public class UserCouponMapper {

    public UserCoupon toDomain(UserCouponEntity userCouponEntity){
        return UserCoupon.builder()
                            .id(userCouponEntity.getId())
                            .discount(userCouponEntity.getDiscount())
                            .couponStatus(userCouponEntity.getCouponStatus())
                            .description(userCouponEntity.getDescription())
                            .build();
    }
    public UserCouponEntity toEntity(UserCoupon userCoupon){
        return UserCouponEntity.builder()
                            .id(userCoupon.getId())
                            .discount(userCoupon.getDiscount())
                            .couponStatus(userCoupon.getCouponStatus())
                            .description(userCoupon.getDescription())
                            .build();
    }
}

