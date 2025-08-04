package kr.hhplus.be.server.payment.domain.Repository;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;

import java.util.Optional;

public interface PaymentRepository {
    Optional<PaymentEntity> findById(long paymentId);
    PaymentEntity save(PaymentEntity payment);
}
