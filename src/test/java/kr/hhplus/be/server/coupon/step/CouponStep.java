package kr.hhplus.be.server.coupon.step;

import kr.hhplus.be.server.coupon.domain.model.*;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;

import java.time.LocalDateTime;

public class CouponStep {

    public static UserCouponCommand 유저쿠폰커맨드_기본값(){
        return new UserCouponCommand(1L, 1L);
    }


    public static UserCouponRequestDTO 유저쿠폰요청_기본값(){
        return UserCouponRequestDTO.builder()
                .userId(1L)
                .couponId(1L)
                .build();
    }

    public static CouponEntity 쿠폰엔티티_기본값(){
        return CouponEntity.builder()
                        .discount(2000L)
                        .description("여름특별할인쿠폰")
                        .remainingQuantity(500L)
                        .expiredAt(LocalDateTime.now().plusMonths(3))
                        .build();
    }


    public static UserCouponEntity 유저쿠폰엔티티_기본값(){
        return UserCouponEntity.builder()
                            .discount(3000L)
                            .couponStatus(CouponStatus.ISSUED)
                            .description("여름 특별 할인 쿠폰")
                            .build();
    }



}
