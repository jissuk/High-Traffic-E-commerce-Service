package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.event.IssueCouponEvent;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;


@UseCase
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final RedisTemplate<String, Long> redis;

    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";

    @DistributedLock
    public void execute(UserCouponCommand command) {
        transactionTemplate.executeWithoutResult(status -> {
            validateDuplicateIssue(command);
            decrementQuantity(command);
            applicationEventPublisher.publishEvent(new IssueCouponEvent(command.userId(), command.couponId()));
        });
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
