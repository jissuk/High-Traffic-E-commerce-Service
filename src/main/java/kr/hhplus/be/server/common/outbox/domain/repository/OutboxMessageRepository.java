package kr.hhplus.be.server.common.outbox.domain.repository;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OutboxMessageRepository {
    OutboxMessage save(OutboxMessage outBoxMessage);
    List<OutboxMessage> findByStatusForUpdate(long limit);
}
