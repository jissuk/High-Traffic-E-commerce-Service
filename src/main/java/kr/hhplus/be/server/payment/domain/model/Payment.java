package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Payment {
    private long id;
    private long price;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    public void complete() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public static Payment createBeforePayment(OrderItemCommand command) {
        return Payment.builder()
                        .price(command.price())
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT)
                        .createdAt(LocalDateTime.now())
                        .build();
    }

}
