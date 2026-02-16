package kr.hhplus.be.server.coupon.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "COUPONS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long discount;
    @Column
    private long quantity;
    @Column
    private String description;
    @Column
    private LocalDateTime expiredAt;

    public void decreaseQuantity() {
        if (quantity <= 0) {
            throw new CouponOutOfStockException();
        }
        this.quantity -= 1;
    }
}