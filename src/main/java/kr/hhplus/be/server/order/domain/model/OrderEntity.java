package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "ORDER")
@Getter
@Setter
@Builder
public class OrderEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    public OrderEntity() {
    }

    public OrderEntity(long id, OrderStatus orderStatus, LocalDateTime createdAt, UserEntity user) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.user = user;
    }

}