package kr.hhplus.be.server.payment.fixture;

import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentFixture {

    private Long amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    private PaymentFixture(){
        this.amount = 50_000L;
        this.paymentStatus = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public static PaymentFixture builder(){
        return new PaymentFixture();
    }

    public PaymentFixture createdAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
        return this;
    }

    public Payment build(){
        return Payment.builder()
                .amount(amount)
                .paymentStatus(paymentStatus)
                .createdAt(createdAt)
                .build();
    }

//    public static Payment withCreatedAt(LocalDateTime localDateTime) {
//        return Payment.builder()
//                .amount(50_000L)
//                .paymentStatus(PaymentStatus.PENDING)
//                .createdAt(localDateTime)
//                .build();
//    }
}
