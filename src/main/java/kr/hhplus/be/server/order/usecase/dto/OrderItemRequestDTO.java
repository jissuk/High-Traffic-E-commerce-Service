package kr.hhplus.be.server.order.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
public class OrderItemRequestDTO {

    @NotNull(message = "상품 ID는 필수입니다.")
    @Min(value = 1, message = "상품 ID는 1 이상이어야 합니다.")
    private Long productId;

    @NotNull(message ="유저 ID는 필수입니다.")
    @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
    private Long userId;

    @NotNull(message = "주문 ID는 필수입니다.")
    @Min(value = 1, message = "주문 ID는 1 이상이어야 합니다.")
    private Long orderId;

    @NotNull(message = "주문상세 ID는 필수입니다.")
    @Min(value = 1, message = "주문상세 ID는 1 이상이어야 합니다.")
    private Long orderItemId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private Long quantity;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long price;

    @NotNull(message = "총 가격은 필수입니다.")
    @Min(value = 0, message = "총 가격은 0 이상이어야 합니다.")
    private Long totalPrice;


    public OrderItemRequestDTO() {
    }

    public OrderItemRequestDTO(Long productId, Long userId, Long orderId, Long orderItemId, Long quantity, Long price, Long totalPrice) {
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderItemRequestDTO{" +
                "productId=" + productId +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", orderItemId=" + orderItemId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
