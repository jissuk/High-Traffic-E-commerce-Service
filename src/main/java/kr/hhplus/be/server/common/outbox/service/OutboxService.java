package kr.hhplus.be.server.common.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.common.outbox.domain.repository.OutboxMessageRepository;
import kr.hhplus.be.server.payment.event.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
* 공용 로직에서는 추상화를 잘 이용하자 ex) 분산락
* */
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final ObjectMapper objectMapper;
    private final OutboxMessageRepository outboxMessageRepository;

    public void save(Object event)  {
        try{
            String payload = objectMapper.writeValueAsString(event);
            OutboxMessage outBoxMessage = OutboxMessage.of(
                    resolveTopic(event),
                    payload
            );
            outboxMessageRepository.save(outBoxMessage);
        } catch (JsonProcessingException e){
            throw new IllegalStateException(e);
        }
    }

    /* event 객체가 Topic 정보를 알고 있음 */
    private String resolveTopic(Object event){
        if (event instanceof PaymentRequestEvent){
            return "payment-request-topic";
        }
        return "";
    }
}
