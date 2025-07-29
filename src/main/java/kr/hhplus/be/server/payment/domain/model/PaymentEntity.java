package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PaymentEntity {
    long id;
    long price;
    PaymentStatus paymentStatus;
    LocalDateTime createAt;

    UserEntity user;
    CouponEntity coupon;
    OrderItemEntity orderItem;

    public PaymentEntity() {
    }

    public PaymentEntity(long id, long price, PaymentStatus paymentStatus, LocalDateTime createAt, UserEntity user, CouponEntity coupon, OrderItemEntity orderItem) {
        this.id = id;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.createAt = createAt;
        this.user = user;
        this.coupon = coupon;
        this.orderItem = orderItem;
    }

    @Override
    public String toString() {
        return "PaymentEntity{" +
                "id=" + id +
                ", price=" + price +
                ", paymentStatus=" + paymentStatus +
                ", createAt=" + createAt +
                ", user=" + user +
                ", coupon=" + coupon +
                ", orderItem=" + orderItem +
                '}';
    }
}
