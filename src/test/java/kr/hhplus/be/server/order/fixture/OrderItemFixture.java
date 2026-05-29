package kr.hhplus.be.server.order.fixture;

import kr.hhplus.be.server.order.domain.model.OrderItem;

public class OrderItemFixture {

    public static OrderItem withProductIdAndQuantity(long productId, long quantity) {
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
