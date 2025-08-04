package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.user.domain.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Order {

    private long id;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;

    public void complete() {
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public static Order createBeforeOrder(User user){
        return Order.builder()
                .orderStatus(OrderStatus.PENDING).build();
    }

}
