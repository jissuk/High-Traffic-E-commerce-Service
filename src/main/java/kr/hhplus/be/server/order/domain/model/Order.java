package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.user.domain.model.User;
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
    private long userId;

    public void complete() {
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public void checkNotCompletedOrder() {
        if(this.orderStatus.equals(OrderStatus.COMPLETED)){
            throw new RuntimeException("이미 완료된 주문입니다.");
        }
    }

    public static Order createBeforeOrder(User user){
        return Order.builder()
                .orderStatus(OrderStatus.PENDING).build();
    }

}
