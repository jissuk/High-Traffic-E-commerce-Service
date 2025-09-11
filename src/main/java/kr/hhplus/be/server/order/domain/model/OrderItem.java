package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.product.domain.model.Product;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class OrderItem {

    private long id;

    private long quantity;

    private long price;

    private long totalPrice;

    private long productId;

    private long orderId;

    public void deductCouponAmount(long discount) {
        this.totalPrice -= discount;

        if(this.totalPrice <= 0) {
            this.totalPrice = 0;
        }
    }

    public static OrderItem createBeforeOrderItem(OrderItemCommand command, Order order) {
        return OrderItem.builder()
                .quantity(command.quantity())
                .price(command.price())
                .totalPrice(command.price() * command.quantity())
                .productId(command.productId())
                .orderId(order.getId())
                .build();
    }


}
