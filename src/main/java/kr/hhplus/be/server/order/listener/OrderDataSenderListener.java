package kr.hhplus.be.server.order.listener;

import kr.hhplus.be.server.payment.event.PaymentCompletedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderDataSenderListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderData(PaymentCompletedEvent event) {
        System.out.println("event :  " + event);
    }
}
