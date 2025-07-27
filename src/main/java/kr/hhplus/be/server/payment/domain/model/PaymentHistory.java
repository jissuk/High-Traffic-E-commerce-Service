package kr.hhplus.be.server.payment.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PaymentHistory {

    long id;
    long price;
    LocalDateTime createdAt;

    PaymentEntity payment;

    public static PaymentHistory of(Payment payment) {
        return PaymentHistory.builder()
                            .price(payment.getPrice())
                            .createdAt(LocalDateTime.now())
                            .build();
    }

    public PaymentHistory() {
    }

    public PaymentHistory(long id, long price, LocalDateTime createdAt, PaymentEntity payment) {
        this.id = id;
        this.price = price;
        this.createdAt = createdAt;
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PaymentHistory{" +
                "id=" + id +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", payment=" + payment +
                '}';
    }
}
