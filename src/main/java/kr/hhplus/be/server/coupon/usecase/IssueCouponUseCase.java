package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.constant.RedisKey;
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

    @DistributedLock(key = "T(kr.hhplus.be.server.common.constant.RedisKey.Coupon).LOCK_COUPON_ISSUE + #command.userId + ':lock'")
    public void execute(UserCouponCommand command) {
        validateDuplicateIssue(command);
        decrementQuantity(command);
        queueCouponIssue(command);
    }


    private void validateDuplicateIssue(UserCouponCommand command) {
        String issuedKey = RedisKey.Coupon.userCouponIssuedKey(command.userId(), command.couponId());
        Boolean issuedResult = redis.opsForValue().getBit(issuedKey, command.userId());

        if(!issuedResult){
            redis.opsForValue().setBit(issuedKey, command.userId(), true);
        } else{
            throw new DuplicateCouponIssueException();
        }
    }

    private void decrementQuantity(UserCouponCommand command) {
        String quantityKey = RedisKey.Coupon.issueCouponQuantityKey(command.couponId());
        Long quantity = redis.opsForValue().decrement(quantityKey);

        if(quantity == null || quantity < 0){
            throw new CouponOutOfStockException();
        }
    }

    private void queueCouponIssue(UserCouponCommand command) {
        String queueKey = RedisKey.Coupon.COUPON_ISSUE_QUEUE;
        String value = command.userId() + ":" + command.couponId();

        couponRedis.opsForZSet().add(queueKey, value,System.currentTimeMillis());
        couponRedis.expire(queueKey, 5, TimeUnit.MINUTES);
    }
}
