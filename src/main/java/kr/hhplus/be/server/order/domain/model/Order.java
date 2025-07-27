package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.user.domain.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Order {

    long id;
    OrderStatus orderStatus;
    LocalDateTime createdAt;

    public void complete() {
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public static Order createBeforeOrder(User user){
        return Order.builder()
                .orderStatus(OrderStatus.PENDING).build();
    }

    public Order() {
    }

    public Order(long id, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
