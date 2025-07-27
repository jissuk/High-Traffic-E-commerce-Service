package kr.hhplus.be.server.order.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderHistory {

    long id;
    OrderStatus orderStatus;
    LocalDateTime createdAt;

    public static OrderHistory of(Order order) {
        return OrderHistory.builder()
                .orderStatus(order.getOrderStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public OrderHistory() {
    }

    public OrderHistory(long id, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
