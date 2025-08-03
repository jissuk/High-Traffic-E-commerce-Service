package kr.hhplus.be.server.coupon.domain.mapper;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    Coupon toDomain(CouponEntity couponEntity);
    CouponEntity toEntity(Coupon coupon);
}

