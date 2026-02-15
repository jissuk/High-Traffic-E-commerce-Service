package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import kr.hhplus.be.server.product.domain.model.Product;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Order {

    private long id;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private long totalPrice;
    private long userId;

    public void complete() {
        if(this.orderStatus.equals(OrderStatus.COMPLETED)){
            throw new RuntimeException("이미 완료된 주문입니다.");
        }
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public static Order createPendingOrder(OrderCommand command, Product product, UserCoupon coupon) {
        long totalPrice = command.quantity() * product.getPrice() - coupon.getDiscount();

        return Order.builder()
                    .orderStatus(OrderStatus.CREATED)
                    .userId(command.userId())
                    .createdAt(LocalDateTime.now())
                    .totalPrice(totalPrice)
                    .build();
    }
}
