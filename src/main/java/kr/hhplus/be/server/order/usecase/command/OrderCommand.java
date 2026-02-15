package kr.hhplus.be.server.order.usecase.command;

import kr.hhplus.be.server.common.provider.LockKeyProvider;
import kr.hhplus.be.server.order.usecase.dto.OrderRequest;
import lombok.Builder;

@Builder
public record OrderCommand(
        Long productId,
        Long userId,
        Long userCouponId,
        Long quantity,
        Long point
        ) implements LockKeyProvider {
    public static OrderCommand from(OrderRequest dto){
        return OrderCommand.builder()
                                .productId(dto.productId())
                                .userId(dto.userId())
                                .userCouponId(dto.userCouponId())
                                .quantity(dto.quantity())
                                .point(dto.point())
                                .build();
    }

    @Override
    public String lockKey() {
        return "order:register:" + productId +":lock";
    }
}

