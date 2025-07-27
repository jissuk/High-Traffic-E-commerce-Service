package kr.hhplus.be.server.coupon.domain.mapper;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserCouponMapper {
    UserCoupon toDomain(UserCouponEntity userCouponEntity);
    UserCouponEntity toEntity(UserCoupon userCoupon);
}