package kr.hhplus.be.server.common.outbox.infrastructure.jpa;

import kr.hhplus.be.server.common.outbox.domain.OutboxStatus;
import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOutBoxMessageRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findByStatus(OutboxStatus outboxStatus);

    List<OutboxMessage> findByPayload(String payload);
}
