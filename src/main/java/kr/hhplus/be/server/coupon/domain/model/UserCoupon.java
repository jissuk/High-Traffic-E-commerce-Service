package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.exception.InvalidCouponException;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class UserCoupon {

    private long id;
    private long discount;
    private CouponStatus couponStatus;
    private String description;
    private long userId;
    private long couponId;

    public void checkCoupon() {
        if (couponStatus.equals(CouponStatus.USED)) {
            throw new InvalidCouponException();
        }
        ;
    }

    public void useCoupon() {
        this.couponStatus = CouponStatus.USED;
    }

    public static UserCoupon createBeforeUserCoupon(Coupon coupon) {
        return UserCoupon.builder()
                            .discount(coupon.getDiscount())
                            .couponStatus(CouponStatus.ISSUED)
                            .description(coupon.getDescription())
                            .build();
    }
}