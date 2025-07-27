package kr.hhplus.be.server.coupon.step;

import kr.hhplus.be.server.coupon.domain.model.*;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;

import java.time.LocalDateTime;

public class CouponStep {

    public static UserCouponRequestDTO 기본유저쿠폰요청생성(){
        return UserCouponRequestDTO.builder()
                .userId(1L)
                .couponId(1L)
                .build();
    }

    public static CouponEntity 기본쿠폰엔티티생성(){
        return CouponEntity.builder()
                        .discount(2000L)
                        .remainingQuantity(1)
                        .description("여름특별할인쿠폰")
                        .remainingQuantity(500L)
                        .expiredAt(LocalDateTime.now().plusMonths(3))
                        .build();
    }


    public static UserCouponEntity 기본유저쿠폰엔티티생성(){
        return UserCouponEntity.builder()
                            .discount(3000L)
                            .couponStatus(CouponStatus.ISSUED)
                            .description("여름 특별 할인 쿠폰")
                            .build();
    }



}
