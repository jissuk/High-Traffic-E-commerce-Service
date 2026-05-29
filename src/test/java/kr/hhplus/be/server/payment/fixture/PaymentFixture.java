package kr.hhplus.be.server.payment.fixture;

import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentFixture {

    public static Payment withCreatedAt(LocalDateTime localDateTime) {
        return Payment.builder()
                .amount(50_000L)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(localDateTime)
                .build();
    }
}
