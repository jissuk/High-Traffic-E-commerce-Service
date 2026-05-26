package kr.hhplus.be.server.coupon.infrastructure.redis;

import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.application.port.out.IssueCouponPort;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueCouponAdapter implements IssueCouponPort {

    private final RedisTemplate<String, Long> redis;

    @Override
    public void validateDuplicateIssue(UserCouponCommand command) {
        String issuedKey = CouponRedisKey.getIssuedKey(command.couponId());
        Boolean issuedResult = redis.opsForValue().getBit(issuedKey, command.userId());

        if(!issuedResult){
            redis.opsForValue().setBit(issuedKey, command.userId(), true);
        } else{
            throw new DuplicateCouponIssueException();
        }
    }

    @Override
    public void decrementQuantity(UserCouponCommand command) {
        String quantityKey = CouponRedisKey.getQuantityKey(command.couponId());
        Long quantity = redis.opsForValue().decrement(quantityKey);
        if(quantity == null || quantity < 0){
            throw new CouponOutOfStockException();
        }
    }
}