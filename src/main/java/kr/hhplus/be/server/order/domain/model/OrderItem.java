package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
public class OrderItem {

    long id;
    long quantity;
    long price;
    long totalPrice;

    public void deductCouponAmount(long discount) {
        this.totalPrice -= discount;

        if(this.totalPrice <= 0) {
            this.totalPrice = 0;
        }
    }

    public void withdrawCouponAmount(long discount) {
        this.totalPrice += discount;
    }

    public static OrderItem createBeforeOrderItem(OrderItemRequestDTO request) {
        return OrderItem.builder()
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .totalPrice(request.getPrice() * request.getQuantity())
                .build();
    }

    public OrderItem() {
    }

    public OrderItem(long totalPrice, long price, long quantity, long id) {
        this.totalPrice = totalPrice;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                '}';
    }

}
