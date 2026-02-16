package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import lombok.*;

@Entity
@Table(name = "ORDER_ITEMS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long quantity;
    @Column
    private long orderId;
    @Column
    private long productId;

    public static OrderItem createBeforeOrderItem(OrderCommand command, Order order) {
        return OrderItem.builder()
                .quantity(command.quantity())
                .productId(command.productId())
                .orderId(order.getId())
                .build();
    }
}
