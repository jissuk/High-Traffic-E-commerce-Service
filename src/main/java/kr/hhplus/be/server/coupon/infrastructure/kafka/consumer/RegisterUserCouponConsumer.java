package kr.hhplus.be.server.coupon.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.application.usecase.RegisterUserCouponUseCase;
import kr.hhplus.be.server.coupon.infrastructure.kafka.CouponTopics;
import kr.hhplus.be.server.coupon.infrastructure.kafka.event.IssueCouponEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterUserCouponConsumer {

    private final ObjectMapper objectMapper;
    private final RegisterUserCouponUseCase useCase;

    @KafkaListener(topics = CouponTopics.COUPON_ISSUE, groupId = "${kafka.consumer.group-id.coupon-issued}")
    public void queueIssueCoupon(String message) throws JsonProcessingException {
        IssueCouponEvent event = objectMapper.readValue(message, IssueCouponEvent.class);
        UserCouponCommand command = new UserCouponCommand(event.userId(), event.couponId());

        useCase.execute(command);
    }
}

