package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Coupon {

    /*
    * 쿠폰 정책
    * remainingQuantity가 null일 경우에는 무제한 발급
    * */
    long id;
    long discount;
    String description;
    long remainingQuantity;
    LocalDateTime expiredAt;

    public void checkQuantity() {
        if (remainingQuantity <= 0) {
            throw new CouponOutOfStockException();
        }
    }

    public void decreaseQuantity() {
        this.remainingQuantity -= 1;
    }

    public Coupon() {
    }

    public Coupon(long id, long discount, String description, long remainingQuantity, LocalDateTime expiredAt) {
        this.id = id;
        this.discount = discount;
        this.description = description;
        this.remainingQuantity = remainingQuantity;
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", discount=" + discount +
                ", description='" + description + '\'' +
                ", remainingQuantity=" + remainingQuantity +
                ", expiredAt=" + expiredAt +
                '}';
    }


}
