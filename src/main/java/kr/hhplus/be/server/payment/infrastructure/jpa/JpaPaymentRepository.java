package kr.hhplus.be.server.payment.infrastructure.jpa;

import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findById(long id);
}
