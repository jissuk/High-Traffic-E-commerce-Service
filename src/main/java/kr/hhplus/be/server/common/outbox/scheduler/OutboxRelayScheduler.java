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
    
    /**
     * 비관적 락을 사용하기 위해서 Transaction을 사용
     * */
    @Scheduled(fixedDelay = 5000)
    public void relay(){
        List<OutboxMessage> messageList = transactionTemplate.execute(status -> {
            List<OutboxMessage> list = outboxMessageRepository.lockPendingMessages(limit);
            list.forEach(OutboxMessage::processing);
            return list;
        });

        for(OutboxMessage message : messageList){
            publish(message);
        }
    }

    public void publish(OutboxMessage message) {
        try {
            kafka.send(message.getTopic(), message.getPayload())
                    .whenComplete((res, ex) -> {
                        if (ex == null) {
                            updatePublished(message.getId());
                        } else{
                            updateFailed(message.getId());
                        }
                    });
        } catch (Exception e) {
            log.error("Kafka send failed", e);
            message.failed();
        }
    }

    /**
     * 비동기 콜백 함수 안에 있기에 트랜잭션을 사용(영속성 컨텍스트 보장)
     * */
    private void updatePublished(Long messageId) {
        transactionTemplate.executeWithoutResult(status -> {
            OutboxMessage message = outboxMessageRepository.findById(messageId);
            message.published();
        });
    }

    private void updateFailed(Long messageId) {
        transactionTemplate.executeWithoutResult(status -> {
            OutboxMessage message = outboxMessageRepository.findById(messageId);
            message.failed();
        });
    }
}