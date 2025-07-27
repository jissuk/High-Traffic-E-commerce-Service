package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.product.domain.model.ProductEntity;
import lombok.*;

@Getter
@Setter
@Builder
public class OrderItemEntity {

    long id;
    long quantity;
    long price;
    long totalPrice;

    OrderEntity order;
    ProductEntity product;

    public OrderItemEntity() {
    }

    public OrderItemEntity(long id, long quantity, long price, long totalPrice, OrderEntity order, ProductEntity product) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.order = order;
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}
