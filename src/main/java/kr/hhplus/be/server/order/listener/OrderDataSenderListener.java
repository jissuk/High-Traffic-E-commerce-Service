package kr.hhplus.be.server.order.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDataSenderListener {

    private final static String PAYMENT_COMPLETE_TOPIC = "paymentComplete";
    private final static String SEND_ORDER_DATA_SERVICE = "sendOrderData-service";

    @KafkaListener(topics = PAYMENT_COMPLETE_TOPIC, groupId = SEND_ORDER_DATA_SERVICE)
    public void sendOrderData(String orderItem) {
        System.out.println("orderItem :  " + orderItem);
    }
}
