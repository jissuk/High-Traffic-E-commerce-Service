package kr.hhplus.be.server.payment.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PaymentHistoryEntity {

    long id;
    long price;
    LocalDateTime createdAt;

    PaymentEntity payment;

    public PaymentHistoryEntity() {
    }

    public PaymentHistoryEntity(long id, long price, LocalDateTime createdAt, PaymentEntity payment) {
        this.id = id;
        this.price = price;
        this.createdAt = createdAt;
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PaymentHistoryEntity{" +
                "id=" + id +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", payment=" + payment +
                '}';
    }
}
