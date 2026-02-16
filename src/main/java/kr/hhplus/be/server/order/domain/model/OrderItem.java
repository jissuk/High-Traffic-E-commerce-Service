package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class OrderItem {
    private long id;
    private long quantity;
    private long productId;
    private long orderId;

    public static OrderItem createBeforeOrderItem(OrderCommand command, Order order) {
        return OrderItem.builder()
                        .quantity(command.quantity())
                        .productId(command.productId())
                        .orderId(order.getId())
                        .build();
    }
}
