package kr.hhplus.be.server.order.fixture;

import kr.hhplus.be.server.order.domain.model.OrderItem;

public class OrderItemFixture {

    private Long quantity;
    private Long productId;

    private OrderItemFixture() {
        this.quantity = 999L;
        this.productId = 999L;
    }

    public static OrderItemFixture builder(){
        return new OrderItemFixture();
    }

    public OrderItemFixture quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderItemFixture productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public OrderItem build(){
        return OrderItem.builder()
                .quantity(quantity)
                .productId(productId)
                .build();
    }
}
