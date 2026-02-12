package kr.hhplus.be.server.common.outbox.scheduler;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.common.outbox.domain.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final KafkaTemplate<String, String> kafka;
    private final OutboxMessageRepository outboxMessageRepository;
    private final TransactionTemplate transactionTemplate;

    private final long limit = 100;

    @Scheduled(fixedDelay = 5000)
    public void relay(){
        List<OutboxMessage> outboxMessageList= outboxMessageRepository.findByStatusForUpdate(limit);

        for (OutboxMessage message : outboxMessageList){
            publish(message);
        }
    }

    public void publish(OutboxMessage message) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                message.processing();

                kafka.send(message.getTopic(), message.getPayload()).get();

                message.published();
            } catch (Exception e) {
                log.error("Kafka send failed", e);
                message.failed();
            }
        });
    }
}