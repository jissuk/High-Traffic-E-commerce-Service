package kr.hhplus.be.server.order.usecase.command;

import kr.hhplus.be.server.order.usecase.dto.OrderItemRequest;
import lombok.Builder;

@Builder
public record OrderItemCommand(Long productId, Long userId, Long orderId, Long orderItemId, Long quantity, Long price, Long totalPrice ) {
    public static OrderItemCommand from(OrderItemRequest dto){
        return OrderItemCommand.builder()
                                .productId(dto.productId())
                                .userId(dto.userId())
                                .orderId(dto.orderId())
                                .quantity(dto.quantity())
                                .price(dto.price())
                                .build();

    }
}

