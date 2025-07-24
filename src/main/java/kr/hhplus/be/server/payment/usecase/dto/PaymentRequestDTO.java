package kr.hhplus.be.server.payment.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {


    @NotNull(message = "결제 ID는 필수입니다.")
    @Min( value = 1, message ="결제 ID는 1 이상이어야 합니다.")
    private Long paymentId;

    @NotNull(message ="유저 ID는 필수입니다.")
    @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
    private Long userId;

    @NotNull(message ="주문 ID는 필수입니다.")
    @Min(value = 1, message = "주문 ID는 1 이상이어야합니다.")
    private Long orderId;

    @NotNull(message ="주문상세 ID는 필수 입니다.")
    @Min(value =1, message = "주문상세 ID는 1 이상이어야 합니다.")
    private Long orderItemId;

    @Min(value =1, message = "쿠폰 ID는 1 이상이어야 합니다.")
    private Long couponId;

    @NotNull(message ="상품 ID는 필수 입니다.")
    @Min(value =1, message = "상품 ID는 1 이상이어야 합니다.")
    private Long productId;
}
