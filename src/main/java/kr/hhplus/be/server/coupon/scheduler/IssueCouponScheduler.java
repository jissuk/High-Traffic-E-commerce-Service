package kr.hhplus.be.server.coupon.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.coupon.usecase.CouponQueueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueCouponScheduler {

    private final CouponQueueUseCase couponQueueUseCase;

    @Scheduled(fixedDelay = 1000)
    public void processQueue() throws JsonProcessingException {
//        couponQueueUseCase.execute();
    }
}
