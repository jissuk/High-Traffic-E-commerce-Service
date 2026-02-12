package kr.hhplus.be.server.order.usecase.command;

import kr.hhplus.be.server.common.provider.LockKeyProvider;
import kr.hhplus.be.server.order.usecase.dto.OrderRequest;
import lombok.Builder;

@Builder
public record OrderCommand(
        Long productId,
        Long userId,
        Long couponId,
        Long quantity,
        Long point
        ) implements LockKeyProvider {
    public static OrderCommand from(OrderRequest dto){
        return OrderCommand.builder()
                                .productId(dto.productId())
                                .userId(dto.userId())
                                .couponId(dto.couponId())
                                .quantity(dto.quantity())
                                .point(dto.point())
                                .build();
    }

    @Override
    public String lockKey() {
        return "order:register:" + productId +":lock";
    }
}

