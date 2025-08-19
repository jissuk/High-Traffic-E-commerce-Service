package kr.hhplus.be.server.coupon.usecase.command;

import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequest;
import lombok.Builder;

@Builder
public record UserCouponCommand(Long userId, Long couponId) {

    public static UserCouponCommand from(UserCouponRequest dto){
        return UserCouponCommand.builder()
                .userId(dto.userId())
                .couponId(dto.couponId())
                .build();
    }
}
