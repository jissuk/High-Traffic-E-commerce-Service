package kr.hhplus.be.server.payment.infrastructure;

import kr.hhplus.be.server.payment.domain.Repository.PaymentHistoryRepository;
import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {

    private final Map<Long, PaymentHistoryEntity> paymentHistoryTable = new HashMap<>();

    @Override
    public PaymentHistoryEntity save(PaymentHistoryEntity paymentHistory) {
        long sequence = 0L;
        sequence++;

        paymentHistory.setId(sequence);
        return paymentHistoryTable.put(paymentHistory.getId(), paymentHistory);
    }
}
