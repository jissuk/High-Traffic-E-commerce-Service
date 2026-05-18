package kr.hhplus.be.server.payment.presentation.dto;

public record TossPaymentConfirmResponse(
        String paymentKey,
        String orderId,
        String status,
        Long totalAmount,
        String method,
        String approvedAt
) {
}
