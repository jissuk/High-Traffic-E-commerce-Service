package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.outbox.service.OutboxService;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.event.IssueCouponEvent;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@UseCase
@RequiredArgsConstructor
public class IssueCouponUseCase {
    private final OutboxService outboxService;
    private final TransactionTemplate transactionTemplate;
    private final RedisTemplate<String, Long> redis;

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";

    @DistributedLock
    public void execute(UserCouponCommand command) {
        /**
         * 1. 중복 쿠폰 발급 여부 (Redis 캐시)
         * 2. 쿠폰 수량 차감     (Redis 캐시)
         * 3. 쿠폰 발급
         * */
        validateDuplicateIssue(command);
        decrementQuantity(command);
        publishIssueUserCouponEvent(command);
    }

    private void validateDuplicateIssue(UserCouponCommand command) {
        String issuedKey = COUPON_ISSUE_PREFIX + command.couponId() + ISSUED_SUFFIX;
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

    private void publishIssueUserCouponEvent(UserCouponCommand command) {
        UserCoupon userCoupon = UserCoupon.of(command);
        IssueCouponEvent event = IssueCouponEvent.of(userCoupon);
        outboxService.save(event);
    }
}
