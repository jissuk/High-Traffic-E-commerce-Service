package kr.hhplus.be.server.order.step;

import kr.hhplus.be.server.order.domain.model.OrderItem;

public class OrderStep {

    public static OrderItem orderItemWithProductIdAndQuantity(long productId, long quantity) {
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
