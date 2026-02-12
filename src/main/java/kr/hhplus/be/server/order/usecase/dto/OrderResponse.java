package kr.hhplus.be.server.order.usecase.dto;


import kr.hhplus.be.server.order.domain.model.OrderItem;
import lombok.Builder;

@Builder
public record OrderResponse(long id, long quantity, long price, long totalPrice, long productId, long orderId) {

    public static OrderResponse from(OrderItem orderItem){
        return OrderResponse.builder()
                                .id(orderItem.getId())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .totalPrice(orderItem.getTotalPrice())
                                .orderId(orderItem.getOrderId())
                                .productId(orderItem.getProductId())
                                .build();
    }
}
