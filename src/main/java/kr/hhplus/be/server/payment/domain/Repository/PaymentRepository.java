package kr.hhplus.be.server.payment.domain.Repository;

import kr.hhplus.be.server.payment.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment findById(long paymentId);
    Payment save(Payment payment);
    Payment findByOrderItemId(Long aLong);
}
