package kr.hhplus.be.server.payment.infrastructure;

import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment findById(long paymentId) {

        return jpaPaymentRepository.findById(paymentId)
                .map(paymentMapper::toDomain)
                .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public Payment save(Payment payment) {

        return paymentMapper.toDomain(
                jpaPaymentRepository.save(paymentMapper.toEntity(payment))
        );
    }

    @Override
    public Payment findByOrderId(Long orderItemId) {
        return jpaPaymentRepository.findByOrderId(orderItemId)
                .map(paymentMapper::toDomain)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
