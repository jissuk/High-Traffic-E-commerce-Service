package kr.hhplus.be.server.coupon.listener;

import kr.hhplus.be.server.coupon.event.IssueCouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class QueueIssueCouponListener {

    private final StringRedisTemplate couponRedis;

    public static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";
    public static final long COUPON_REQUEST_EXPIRE_MINUTES = 5;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void queueIssueCoupon(IssueCouponEvent event){
        String value = event.userId() + ":" + event.couponId();

        couponRedis.opsForZSet().add(COUPON_ISSUE_QUEUE, value,System.currentTimeMillis());
        couponRedis.expire(COUPON_ISSUE_QUEUE, COUPON_REQUEST_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

}
