package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;


@UseCase
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final RedisTemplate<String, Long> redis;
    private final StringRedisTemplate couponRedis;

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";
    public static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";

    public static final long COUPON_REQUEST_EXPIRE_MINUTES = 5;

    @DistributedLock(key = "T(kr.hhplus.be.server.common.constant.RedisKey.Coupon).LOCK_COUPON_ISSUE + #command.userId + ':lock'")
    public void execute(UserCouponCommand command) {
        validateDuplicateIssue(command);
        decrementQuantity(command);
        queueCouponIssue(command);
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
        String quantityKey = ISSUED_SUFFIX + command.couponId() + QUANTITY_SUFFIX;

        Long quantity = redis.opsForValue().decrement(quantityKey);

        if(quantity == null || quantity < 0){
            throw new CouponOutOfStockException();
        }
    }

    private void queueCouponIssue(UserCouponCommand command) {
        String value = command.userId() + ":" + command.couponId();

        couponRedis.opsForZSet().add(COUPON_ISSUE_QUEUE, value,System.currentTimeMillis());
        couponRedis.expire(COUPON_ISSUE_QUEUE, COUPON_REQUEST_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }
}
