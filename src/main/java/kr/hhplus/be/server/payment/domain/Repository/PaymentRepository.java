package kr.hhplus.be.server.payment.domain.Repository;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;

public interface PaymentRepository {
    PaymentEntity findById(long paymentId);
    PaymentEntity save(PaymentEntity payment);
}
