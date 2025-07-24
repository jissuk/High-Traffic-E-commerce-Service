package kr.hhplus.be.server.payment.infrastructure;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final Map<Long, PaymentEntity> paymentTable = new HashMap<>();

    @Override
    public PaymentEntity findById(Long paymentId) {
        return paymentTable.get(paymentId);
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        long sequence = 0L;
        sequence++;

        payment.setId(sequence);
        return paymentTable.put(payment.getId(), payment);
    }

    @Override
    public PaymentEntity update(PaymentEntity payment) {
        return paymentTable.put(payment.getId(), payment);
    }

    @Override
    public void clear() {
        paymentTable.clear();
    }

}
