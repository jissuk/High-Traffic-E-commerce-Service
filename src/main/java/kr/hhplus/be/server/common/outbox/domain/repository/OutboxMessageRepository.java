package kr.hhplus.be.server.common.outbox.domain.repository;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import java.util.List;

public interface OutboxMessageRepository {
    OutboxMessage findById(Long messageId);
    OutboxMessage save(OutboxMessage outBoxMessage);
    List<OutboxMessage> lockPendingMessages(long limit);
}
