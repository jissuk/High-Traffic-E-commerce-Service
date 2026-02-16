package kr.hhplus.be.server.payment.domain.mapper;

import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toDomain(PaymentEntity paymentEntity){
        return Payment.builder()
                        .id(paymentEntity.getId())
                        .amount(paymentEntity.getAmount())
                        .tossOrderId(paymentEntity.getTossOrderId())
                        .tossPaymentKey(paymentEntity.getTossPaymentKey())
                        .paymentStatus(paymentEntity.getPaymentStatus())
                        .createdAt(paymentEntity.getCreateAt())
                        .userId(paymentEntity.getUserId())
                        .orderId(paymentEntity.getOrderId())
                        .build();
    }

    public PaymentEntity toEntity(Payment payment){
        return PaymentEntity.builder()
                            .id(payment.getId())
                            .amount(payment.getAmount())
                            .tossOrderId(payment.getTossOrderId())
                            .tossPaymentKey(payment.getTossPaymentKey())
                            .paymentStatus(payment.getPaymentStatus())
                            .createAt(payment.getCreatedAt())
                            .userId(payment.getUserId())
                            .orderId(payment.getOrderId())
                            .build();
    }
}

