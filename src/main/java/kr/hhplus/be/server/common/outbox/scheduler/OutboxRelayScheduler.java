package kr.hhplus.be.server.common.outbox.scheduler;

import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.common.outbox.domain.repository.OutboxMessageRepository;
import kr.hhplus.be.server.common.outbox.domain.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final KafkaTemplate<String, String> kafka;
    private final OutboxMessageRepository outboxMessageRepository;

    @Scheduled(fixedDelay = 5000)
    public void relayMessages() {
        List<OutboxMessage> outboxList = outboxMessageRepository.findByStatus(OutboxStatus.PENDING);

        for(OutboxMessage outbox : outboxList) {
            CompletableFuture<SendResult<String, String>> send = kafka.send(outbox.getTopic(), outbox.getPayload());

            send.whenComplete((result, ex) -> {
                if (ex == null) {
                    outbox.published();
                    outboxMessageRepository.save(outbox);
                }
            });
        }
    }
}