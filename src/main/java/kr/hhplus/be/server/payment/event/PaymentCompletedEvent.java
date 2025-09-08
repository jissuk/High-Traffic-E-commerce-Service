package kr.hhplus.be.server.payment.event;

import kr.hhplus.be.server.order.domain.model.OrderItem;

public record PaymentCompletedEvent(
        long orderItemId,
        long orderItemQuantity,
        long orderItemPrice,
        long orderItemTotalPrice,
        long orderId,
        long productId) {
    public static PaymentCompletedEvent from(OrderItem orderItem) {
        return new PaymentCompletedEvent(
            orderItem.getId(),
            orderItem.getQuantity(),
            orderItem.getPrice(),
            orderItem.getTotalPrice(),
            orderItem.getOrderId(),
            orderItem.getProductId()
        );
    }
}
