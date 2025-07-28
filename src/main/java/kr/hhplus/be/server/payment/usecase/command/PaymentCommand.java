package kr.hhplus.be.server.payment.usecase.command;

import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;

public record PaymentCommand(Long paymentId, Long userId, Long orderId, Long orderItemId, Long couponId, Long productId) {
    public static PaymentCommand from(PaymentRequestDTO dto) {
        return new  PaymentCommand(dto.getPaymentId(), dto.getUserId(), dto.getOrderId(), dto.getOrderItemId(), dto.getCouponId(), dto.getProductId());
    }

}
