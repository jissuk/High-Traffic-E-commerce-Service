package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
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
    private long orderId;

    public void validateTossPaymentConsistency(Long amount) {
        if(!this.amount.equals(amount)) {
            throw new RuntimeException("결제 금액이 일치하지 않습니다.");
        }
    }

    public void requested(){
        if(this.paymentStatus.equals(PaymentStatus.REQUESTED)){
            throw new RuntimeException("이미 요청 중인 결제입니다.");
        }
        this.paymentStatus = PaymentStatus.REQUESTED;
    }

    public static Payment createBeforePayment(OrderCommand command, Order order) {
        return Payment.builder()
                        .amount(order.getTotalPrice())
                        .paymentStatus(PaymentStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .userId(command.userId())
                        .orderId(order.getId())
                        .build();
    }
}
