package kr.hhplus.be.server.payment.infrastructure;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;

    public PaymentRepositoryImpl(JpaPaymentRepository jpaPaymentRepository) {
        this.jpaPaymentRepository = jpaPaymentRepository;
    }

    @Override
    public Optional<PaymentEntity> findById(long paymentId) {

        return jpaPaymentRepository.findById(paymentId);
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        return jpaPaymentRepository.save(payment);
    }

}
