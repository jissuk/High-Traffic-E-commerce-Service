package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderEntity {

    long id;
    OrderStatus orderStatus;
    LocalDateTime createdAt;

    UserEntity user;

    public OrderEntity() {
    }

    public OrderEntity(long id, OrderStatus orderStatus, LocalDateTime createdAt, UserEntity user) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
