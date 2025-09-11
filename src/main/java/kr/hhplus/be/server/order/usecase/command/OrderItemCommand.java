package kr.hhplus.be.server.order.usecase.command;

import kr.hhplus.be.server.common.provider.LockKeyProvider;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequest;
import lombok.Builder;

@Builder
public record OrderItemCommand(Long productId, Long userId, Long orderItemId, Long quantity, Long price, Long totalPrice ) implements LockKeyProvider {
    public static OrderItemCommand from(OrderItemRequest dto){
        return OrderItemCommand.builder()
                                .productId(dto.productId())
                                .userId(dto.userId())
                                .quantity(dto.quantity())
                                .price(dto.price())
                                .build();
    }

    @Override
    public String lockKey() {
        return "order:register:" + productId +":lock";
    }
}

