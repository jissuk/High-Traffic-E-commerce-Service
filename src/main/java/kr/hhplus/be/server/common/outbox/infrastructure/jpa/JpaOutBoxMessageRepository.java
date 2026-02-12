package kr.hhplus.be.server.common.outbox.infrastructure.jpa;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaOutBoxMessageRepository extends JpaRepository<OutboxMessage, Long> {
    @Query(
            value = "SELECT * FROM outbox_messages WHERE status = 'PENDING' ORDER BY id FOR UPDATE SKIP LOCKED LIMIT :limit",
            nativeQuery = true
    )
    List<OutboxMessage> findByStatusForUpdate(long limit);
}
