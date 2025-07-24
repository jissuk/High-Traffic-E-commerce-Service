package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.exception.InvalidCouponException;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.user.domain.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCoupon {

    long id;
    long discount;
    CouponStatus couponStatus;
    String description;

    public void checkCoupon(){
        if(!couponStatus.equals(CouponStatus.ISSUED)){
            throw new InvalidCouponException();
        };
    }

    public static UserCoupon createBeforeUserCoupon(Coupon coupon) {
        return UserCoupon.builder()
                .discount(coupon.getDiscount())
                .couponStatus(CouponStatus.ISSUED)
                .description(coupon.getDescription())
                .build();
    }

    public UserCoupon() {
    }

    public UserCoupon(long id, long discount, CouponStatus couponStatus, String description) {
        this.id = id;
        this.discount = discount;
        this.couponStatus = couponStatus;
        this.description = description;
    }

    @Override
    public String toString() {
        return "UserCoupon{" +
                "id=" + id +
                ", discount=" + discount +
                ", couponStatus=" + couponStatus +
                ", description='" + description + '\'' +
                '}';
    }
}
