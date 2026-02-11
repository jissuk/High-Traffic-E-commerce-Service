package kr.hhplus.be.server.payment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.payment.event.PaymentApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovePaymentListener {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.payment-approved}", groupId = "${kafka.consumer.group-id.payment-approved}")
    public void approvedPayment(String message) throws JsonProcessingException {
        PaymentApprovedEvent event = objectMapper.readValue(message, PaymentApprovedEvent.class);

    }
}
