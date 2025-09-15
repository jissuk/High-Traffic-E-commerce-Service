package kr.hhplus.be.server.order.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record OrderItemRequest(
        @NotNull(message = "상품 ID는 필수입니다.")
        @Min(value = 1, message = "상품 ID는 1 이상이어야 합니다.")
        Long productId,

        @NotNull(message ="유저 ID는 필수입니다.")
        @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
        Long userId,

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        Long quantity,

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        Long price
) {}
