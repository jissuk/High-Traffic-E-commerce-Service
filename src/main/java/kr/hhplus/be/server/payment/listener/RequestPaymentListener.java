package kr.hhplus.be.server.payment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.config.toss.TossProperties;
import kr.hhplus.be.server.payment.event.PaymentRequestEvent;
import kr.hhplus.be.server.payment.usecase.dto.PaymentConfirmRequest;
import kr.hhplus.be.server.payment.usecase.dto.TossPaymentConfirmResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestPaymentListener {
    private final TossProperties tossProperties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @KafkaListener(topics = "payment-request-topic", groupId = "${kafka.consumer.group-id.payment-request}")
    public void requestPayment(String message) throws JsonProcessingException {
        PaymentRequestEvent event = objectMapper.readValue(message, PaymentRequestEvent.class);
        TossPaymentConfirmResponse response = tossPaymentConfirmRequest(event);
    }

    private TossPaymentConfirmResponse tossPaymentConfirmRequest(PaymentRequestEvent event) {
        String auth = tossProperties.secretApiKey() + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        PaymentConfirmRequest request = new PaymentConfirmRequest(event.tossOrderId(), event.tossPaymentKey(), event.amount());
        HttpEntity<PaymentConfirmRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TossPaymentConfirmResponse> response = restTemplate.exchange(tossProperties.approveUrl(),
                                                                                    HttpMethod.POST,
                                                                                    entity,
                                                                                    TossPaymentConfirmResponse.class
        );

        return response.getBody();
    }
}
