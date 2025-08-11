package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Payment {
    private long id;
    private long price;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private long userId;
    private long orderItemId;

    public void complete() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void checkPayment() {
        if(this.paymentStatus.equals(PaymentStatus.COMPLETED)){
            throw new RuntimeException("이미 완료된 결제입니다.");
        }
    }

    public static Payment createBeforePayment(OrderItemCommand command) {
        return Payment.builder()
                        .price(command.price())
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                        .createdAt(LocalDateTime.now())
                        .build();
    }

}
