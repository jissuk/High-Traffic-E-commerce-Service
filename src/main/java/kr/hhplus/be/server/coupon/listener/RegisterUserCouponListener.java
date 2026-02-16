package kr.hhplus.be.server.coupon.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterUserCouponListener {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.coupon-issued}", groupId = "${kafka.consumer.group-id.coupon-issued}")
    public void queueIssueCoupon(String message) throws JsonProcessingException {
        UserCouponCommand userCommand = objectMapper.readValue(message, UserCouponCommand.class);

        User user = userRepository.findById(userCommand.userId());
        Coupon coupon = couponRepository.findById(userCommand.couponId());

        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon, user);
        userCouponRepository.save(userCoupon);
    }
}

