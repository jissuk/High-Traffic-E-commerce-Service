package kr.hhplus.be.server.payment.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PaymentRequest(
        @NotNull(message = "결제 ID는 필수입니다.")
        @Min( value = 1, message ="결제 ID는 1 이상이어야 합니다.")
        Long paymentId,

        @NotNull(message ="유저 ID는 필수입니다.")
        @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
        Long userId,

        @NotNull(message ="주문 ID는 필수입니다.")
        @Min(value = 1, message = "주문 ID는 1 이상이어야합니다.")
        Long orderId,

        @NotNull(message ="주문상세 ID는 필수 입니다.")
        @Min(value =1, message = "주문상세 ID는 1 이상이어야 합니다.")
        Long orderItemId,

        @Min(value =1, message = "쿠폰 ID는 1 이상이어야 합니다.")
        Long couponId,

        @NotNull(message ="상품 ID는 필수 입니다.")
        @Min(value =1, message = "상품 ID는 1 이상이어야 합니다.")
        Long productId
) {}
