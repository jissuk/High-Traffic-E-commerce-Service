package kr.hhplus.be.server.common.outbox.domain.repository;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.common.outbox.domain.OutboxStatus;

import java.util.List;

public interface OutboxMessageRepository {
    List<OutboxMessage> findByStatus(OutboxStatus outboxStatus);
    OutboxMessage save(OutboxMessage outBoxMessage);

}
