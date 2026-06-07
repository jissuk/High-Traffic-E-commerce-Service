package kr.hhplus.be.server.coupon.fixture;

import kr.hhplus.be.server.coupon.domain.model.Coupon;

import java.time.LocalDateTime;

public class CouponFixture {

    private Long discount;
    private Long quantity;
    private String description;
    private LocalDateTime expiredAt;

    // 객체가 생성될 때 기본 값 주입
    private CouponFixture() {
        this.discount = 3000L;
        this.quantity = 500L;
        this.description = "여름 특별 할인 쿠폰";
        this.expiredAt = LocalDateTime.now().plusMonths(3);
    }

    public static CouponFixture builder() {
        return new CouponFixture();
    }

    // 특정 값을 주입해야 하는 경우 사용
    public CouponFixture quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public Coupon build() {
        return Coupon.builder()
                .discount(discount)
                .quantity(quantity)
                .description(description)
                .expiredAt(expiredAt)
                .build();
    }
}
