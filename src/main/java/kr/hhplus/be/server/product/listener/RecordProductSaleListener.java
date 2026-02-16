//package kr.hhplus.be.server.product.listener;
//
//import kr.hhplus.be.server.payment.event.PaymentCompletedEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionPhase;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@RequiredArgsConstructor
//public class RecordProductSaleListener {
//
//    private final RedisTemplate<String, Long> redis;
//
//    public static final String PRODUCT_SALES_PREFIX = "product:sales:";
//
//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void recordProductSale(PaymentCompletedEvent event) {
//        String zSetKey = PRODUCT_SALES_PREFIX + LocalDate.now(ZoneId.of("Asia/Seoul"));
//        redis.opsForZSet().incrementScore(zSetKey, event.productId(), event.orderItemQuantity());
//
//        long baseTtlSeconds = TimeUnit.DAYS.toSeconds(5);
//
//        redis.expire(zSetKey, baseTtlSeconds, TimeUnit.SECONDS);
//    }
//}
