package kr.hhplus.be.server.external.dataplatform;

import kr.hhplus.be.server.common.sender.OrderDataSender;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpDataSender implements OrderDataSender {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DATA_PLATFORM_URL = "https://dataplatform.com/orders";

    @Override
    public void send(OrderItem orderItem) {
        restTemplate.postForEntity(DATA_PLATFORM_URL, orderItem, Void.class);
    }
}
