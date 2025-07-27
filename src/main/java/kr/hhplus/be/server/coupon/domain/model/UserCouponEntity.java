package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCouponEntity {
    long id;
    long discount;
    CouponStatus couponStatus;
    String description;

    CouponEntity coupon;
    UserEntity user;

    public UserCouponEntity() {
    }

    public UserCouponEntity(long id, long discount, CouponStatus couponStatus, String description, CouponEntity coupon, UserEntity user) {
        this.id = id;
        this.discount = discount;
        this.couponStatus = couponStatus;
        this.description = description;
        this.coupon = coupon;
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserCouponEntity{" +
                "id=" + id +
                ", discount=" + discount +
                ", couponStatus=" + couponStatus +
                ", description='" + description + '\'' +
                ", coupon=" + coupon +
                ", user=" + user +
                '}';
    }
}
