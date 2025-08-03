package kr.hhplus.be.server.payment.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_PAYMENT")
@Getter
@Setter
@Builder
public class PaymentEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    CouponEntity coupon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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

}
