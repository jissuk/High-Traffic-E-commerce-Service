package kr.hhplus.be.server.common.outbox.scheduler;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.common.outbox.domain.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final KafkaTemplate<String, String> kafka;
    private final OutboxMessageRepository outboxMessageRepository;
    private final TransactionTemplate transactionTemplate;

    private static final long LIMIT = 100;
    private static final long BASE_DELAY_SECONDS = 1;  // 초
    private static final long MAX_DELAY_SECONDS = 300; // 5분

    /**
     * 비관적 락을 사용하기 위해서 Transaction을 사용
     * */
    @Scheduled(fixedDelay = 5000)
    public void relay(){
        List<OutboxMessage> messageList = transactionTemplate.execute(status -> {
            List<OutboxMessage> list = outboxMessageRepository.lockPendingMessages(LIMIT);
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

            long currentRetryCount = message.getRetryCount();
            long maxRetryCount = message.getMaxRetryCount();

            if(maxRetryCount <= currentRetryCount){
                message.dead();
                return;
            }

            long nextRetryCount = currentRetryCount + 1;
            LocalDateTime nextRetryAt = calculateNextRetryAt(nextRetryCount);

            message.retry(nextRetryAt, nextRetryCount);
        });
    }

    private LocalDateTime calculateNextRetryAt(long nextRetryCount) {
        long backoffDelay = BASE_DELAY_SECONDS * (1L << (nextRetryCount - 1)); // 1, 2, 4, 8....
        long delay = Math.min(backoffDelay, MAX_DELAY_SECONDS);
        return LocalDateTime.now().plusSeconds(delay);
    }
}