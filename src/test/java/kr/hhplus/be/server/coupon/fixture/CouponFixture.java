package kr.hhplus.be.server.coupon.fixture;

import kr.hhplus.be.server.coupon.domain.model.Coupon;

import java.time.LocalDateTime;

public class CouponFixture {

    public static Coupon create() {
        return Coupon.builder()
                .discount(3000L)
                .quantity(500L)
                .description("여름 특별 할인 쿠폰")
                .expiredAt(LocalDateTime.now().plusMonths(3))
                .build();
    }
}
