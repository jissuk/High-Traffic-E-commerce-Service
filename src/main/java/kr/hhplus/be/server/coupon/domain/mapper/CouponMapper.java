package kr.hhplus.be.server.coupon.domain.mapper;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {
    public Coupon toDomain(CouponEntity couponEntity){
        return Coupon.builder()
                    .id(couponEntity.getId())
                    .discount(couponEntity.getDiscount())
                    .quantity(couponEntity.getQuantity())
                    .description(couponEntity.getDescription())
                    .expiredAt(couponEntity.getExpiredAt())
                    .build();
    }

    public CouponEntity toEntity(Coupon coupon){
        return CouponEntity.builder()
                            .id(coupon.getId())
                            .discount(coupon.getDiscount())
                            .quantity(coupon.getQuantity())
                            .description(coupon.getDescription())
                            .expiredAt(coupon.getExpiredAt())
                            .build();
    }
}

