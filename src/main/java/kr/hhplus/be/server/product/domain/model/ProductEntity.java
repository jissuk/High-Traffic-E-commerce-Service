package kr.hhplus.be.server.product.domain.model;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.product.exception.InsufficientStockException;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductEntity {
    long id;
    String productName;
    long price;
    long quantity;

    List<OrderItemEntity> orderItemList;


    public ProductEntity() {
    }

    public ProductEntity(long id, String productName, long price, long quantity, List<OrderItemEntity> orderItemList) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
