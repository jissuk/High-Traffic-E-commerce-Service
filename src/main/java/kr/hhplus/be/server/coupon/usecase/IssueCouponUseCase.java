package kr.hhplus.be.server.coupon.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

@UseCase
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final RedisTemplate<String, Long> redis;
    private final KafkaTemplate<String, UserCouponCommand> kafka;

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";
    public static final String ISSUE_COUPON_TOPIC = "issueCoupon";

    @DistributedLock
    public void execute(UserCouponCommand command) throws JsonProcessingException {
        validateDuplicateIssue(command);
        decrementQuantity(command);
        kafka.send(ISSUE_COUPON_TOPIC, command);
    }

    private void validateDuplicateIssue(UserCouponCommand command) {
        String issuedKey = COUPON_ISSUE_PREFIX + command.userId() + ISSUED_SUFFIX;
        Boolean issuedResult = redis.opsForValue().getBit(issuedKey, command.userId());

        if(!issuedResult){
            redis.opsForValue().setBit(issuedKey, command.userId(), true);
        } else{
            throw new DuplicateCouponIssueException();
        }
    }

    private void decrementQuantity(UserCouponCommand command) {
        String quantityKey = COUPON_ISSUE_PREFIX + command.couponId() + QUANTITY_SUFFIX;
        Long quantity = redis.opsForValue().decrement(quantityKey);
        if(quantity == null || quantity < 0){
            throw new CouponOutOfStockException();
        }
    }
}
