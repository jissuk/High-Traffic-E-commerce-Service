package kr.hhplus.be.server.coupon.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CouponEntity {

    long id;
    long discount;
    String description;
    long remainingQuantity;
    LocalDateTime expiredAt;

    public CouponEntity() {
    }

    public CouponEntity(long id, long discount, String description, long remainingQuantity, LocalDateTime expiredAt) {
        this.id = id;
        this.discount = discount;
        this.description = description;
        this.remainingQuantity = remainingQuantity;
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "CouponEntity{" +
                "id=" + id +
                ", discount=" + discount +
                ", description='" + description + '\'' +
                ", remainingQuantity=" + remainingQuantity +
                ", expiredAt=" + expiredAt +
                '}';
    }

}
