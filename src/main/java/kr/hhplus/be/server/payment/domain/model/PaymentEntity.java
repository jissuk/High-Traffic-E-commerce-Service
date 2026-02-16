package kr.hhplus.be.server.payment.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENTS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long amount;
    @Column
    private String tossOrderId;
    @Column
    private String tossPaymentKey;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column
    private LocalDateTime createAt;
    @Column
    private long userId;
    @Column
    private long orderId;
}