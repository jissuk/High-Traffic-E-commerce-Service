package kr.hhplus.be.server.coupon.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.exception.InvalidCouponException;
import kr.hhplus.be.server.user.domain.model.User;
import lombok.*;

@Entity
@Table(name = "USER_COUPONS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserCoupon {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long discount;
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;
    @Column
    private String description;
    @Column
    private long couponId;
    @Column
    private long userId;
    @Version
    private long version;

    public void useCoupon() {
        if (couponStatus.equals(CouponStatus.USED)) {
            throw new InvalidCouponException();
        };
        this.couponStatus = CouponStatus.USED;
    }

    public static UserCoupon createBeforeUserCoupon(Coupon coupon, User user) {
        return UserCoupon.builder()
                .discount(coupon.getDiscount())
                .couponStatus(CouponStatus.ISSUED)
                .description(coupon.getDescription())
                .userId(user.getId())
                .couponId(coupon.getId())
                .build();
    }
}
