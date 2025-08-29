package kr.hhplus.be.server.payment.event;

public record PaymentCompletedEvent(
        long orderItemId,
        long orderItemQuantity,
        long orderItemPrice,
        long orderItemTotalPrice,
        long orderId,
        long productId) {

}
