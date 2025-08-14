package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.exception.InvalidCouponException;
import kr.hhplus.be.server.user.domain.model.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class UserCoupon {

    private long id;
    private long discount;
    private CouponStatus couponStatus;
    private String description;
    private long userId;
    private long couponId;

    public void useCheckCoupon() {
        if (couponStatus.equals(CouponStatus.USED)) {
            throw new InvalidCouponException();
        }
        ;
    }

    public void useCoupon() {
        this.couponStatus = CouponStatus.USED;
    }

    public static UserCoupon createBeforeUserCoupon(Coupon coupon, User user) {
        return UserCoupon.builder()
                            .discount(coupon.getDiscount())
                            .couponStatus(CouponStatus.ISSUED)
                            .description(coupon.getDescription())
                            .userId(user.getId())
                            .build();
    }
}