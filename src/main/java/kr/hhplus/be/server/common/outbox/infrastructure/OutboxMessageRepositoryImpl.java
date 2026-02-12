package kr.hhplus.be.server.common.outbox.infrastructure;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.common.outbox.domain.repository.OutboxMessageRepository;
import kr.hhplus.be.server.common.outbox.infrastructure.jpa.JpaOutBoxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class OutboxMessageRepositoryImpl implements OutboxMessageRepository {

    private final JpaOutBoxMessageRepository jpaOutBoxMessageRepository;

    @Override
    public OutboxMessage save(OutboxMessage outBoxMessage) {
        return jpaOutBoxMessageRepository.save(outBoxMessage);
    }

    @Override
    public List<OutboxMessage> findByStatusForUpdate(long limit) {
        return jpaOutBoxMessageRepository.findByStatusForUpdate(limit);
    }

}
