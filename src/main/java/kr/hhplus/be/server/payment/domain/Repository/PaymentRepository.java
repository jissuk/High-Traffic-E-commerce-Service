package kr.hhplus.be.server.payment.domain.Repository;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;

public interface PaymentRepository {
    PaymentEntity findById(Long paymentId);
    PaymentEntity save(PaymentEntity payment);
    PaymentEntity update(PaymentEntity payment);

    void clear();
}
