package kr.hhplus.be.server.payment.infrastructure.jpa;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity findById(long id);
}
