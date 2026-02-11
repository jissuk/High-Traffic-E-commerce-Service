package kr.hhplus.be.server.payment.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TossPaymentConfirmRequest(
        @NotNull(message = "주문 ID는 필수입니다.")
        String orderId,

        @NotNull(message = "결제 KEY는 필수입니다.")
        String paymentKey,

        @NotNull(message ="총 금액은 필수입니다.")
        @Min( value = 1, message ="총 금액은 1 이상이어야 합니다.")
        Long amount
) {
}
