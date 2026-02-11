package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Payment {
    private long id;
    private Long amount;
    private String tossOrderId;
    private String tossPaymentKey;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private long userId;
    private long orderItemId;

    public void approve() {
        this.paymentStatus = PaymentStatus.APPROVED;
    }

    public void validateStatus() {
        if(this.paymentStatus.equals(PaymentStatus.APPROVED)){
            throw new RuntimeException("이미 완료된 결제입니다.");
        }
    }

    public void validateAmount(Long amount) {
        if(!this.amount.equals(amount)){
            throw new RuntimeException("결제하려는 금액과 일치하지 않습니다.");
        }
    }

    public static Payment createBeforePayment(OrderItemCommand command, OrderItem orderItem) {
        return Payment.builder()
                        .amount(command.price())
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                        .createdAt(LocalDateTime.now())
                        .userId(command.userId())
                        .orderItemId(orderItem.getId())
                        .build();
    }
}
