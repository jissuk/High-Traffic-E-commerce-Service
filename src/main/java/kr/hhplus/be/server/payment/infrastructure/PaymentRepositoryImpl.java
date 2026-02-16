package kr.hhplus.be.server.payment.infrastructure;

import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;

    @Override
    public Payment findById(long paymentId) {

        return jpaPaymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public Payment save(Payment payment) {
        return jpaPaymentRepository.save(payment);
    }

    @Override
    public Payment findByOrderId(Long orderItemId) {
        return jpaPaymentRepository.findByOrderId(orderItemId)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
