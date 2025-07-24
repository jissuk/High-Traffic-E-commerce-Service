package kr.hhplus.be.server.order.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderHistoryEntity {

    long id;
    OrderStatus orderStatus;
    LocalDateTime createdAt;

    OrderEntity order;

    public OrderHistoryEntity() {
    }

    public OrderHistoryEntity(long id, OrderStatus orderStatus, LocalDateTime createdAt, OrderEntity order) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderHistoryEntity{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                ", order=" + order +
                '}';
    }
}
