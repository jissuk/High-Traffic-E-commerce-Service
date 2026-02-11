package kr.hhplus.be.server.payment.domain.mapper;

import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toDomain(PaymentEntity paymentEntity){
        return Payment.builder()
                        .id(paymentEntity.getId())
                        .amount(paymentEntity.getPrice())
                        .paymentStatus(paymentEntity.getPaymentStatus())
                        .createdAt(paymentEntity.getCreateAt())
                        .userId(paymentEntity.getUserId())
                        .orderItemId(paymentEntity.getOrderItemId())
                        .build();
    }

    public PaymentEntity toEntity(Payment payment){
        return PaymentEntity.builder()
                            .id(payment.getId())
                            .price(payment.getAmount())
                            .paymentStatus(payment.getPaymentStatus())
                            .createAt(payment.getCreatedAt())
                            .userId(payment.getUserId())
                            .orderItemId(payment.getOrderItemId())
                            .build();
    }
}

