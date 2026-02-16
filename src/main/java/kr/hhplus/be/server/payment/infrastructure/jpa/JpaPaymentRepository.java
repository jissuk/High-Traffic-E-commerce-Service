package kr.hhplus.be.server.payment.infrastructure.jpa;

import kr.hhplus.be.server.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(long id);
    Optional<Payment> findByOrderId(Long orderItemId);
}
