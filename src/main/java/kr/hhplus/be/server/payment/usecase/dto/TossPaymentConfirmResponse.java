package kr.hhplus.be.server.payment.usecase.dto;

public record TossPaymentConfirmResponse(
        String paymentKey,
        String orderId,
        String status,
        Long totalAmount,
        String method,
        String approvedAt
) {
}
