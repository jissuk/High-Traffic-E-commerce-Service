package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
public class Payment {
    long id;
    long price;
    PaymentStatus paymentStatus;

    public void complete() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void beforePayment() {
        this.paymentStatus = PaymentStatus.BEFORE_PAYMENT;
    }

    public static Payment createBeforePayment(OrderItemCommand command) {
        return Payment.builder()
                        .price(command.price())
                        .paymentStatus(PaymentStatus.BEFORE_PAYMENT).build();
    }

    public Payment() {
    }

    public Payment(long id, long price, PaymentStatus paymentStatus) {
        this.id = id;
        this.price = price;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", price=" + price +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
