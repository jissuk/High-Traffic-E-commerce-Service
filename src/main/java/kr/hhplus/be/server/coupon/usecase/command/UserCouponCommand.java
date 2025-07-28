package kr.hhplus.be.server.coupon.usecase.command;

import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;

public record UserCouponCommand(Long userId, Long couponId) {

    public static UserCouponCommand from(UserCouponRequestDTO dto){
        return new UserCouponCommand(dto.getUserId(), dto.getCouponId());
    }
}
