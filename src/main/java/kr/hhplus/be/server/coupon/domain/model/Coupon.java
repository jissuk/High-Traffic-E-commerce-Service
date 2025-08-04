package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Coupon {

    private long id;
    private long discount;
    private long quantity;
    private String description;
    private LocalDateTime expiredAt;

    public void checkQuantity() {
        if (quantity <= 0) {
            throw new CouponOutOfStockException();
        }
    }
    public void decreaseQuantity() {
        this.quantity -= 1;
    }
}
