package kr.hhplus.be.server.payment.domain.Repository;

import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;

public interface PaymentHistoryRepository {
    PaymentHistoryEntity save(PaymentHistoryEntity paymentHistory);
}
