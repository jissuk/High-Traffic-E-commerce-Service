package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

import static org.springframework.data.redis.core.ZSetOperations.TypedTuple;

@UseCase
@RequiredArgsConstructor
public class CouponQueueUseCase {

    private final StringRedisTemplate couponRedis;

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";
    public static final long REQUEST_BATCH_SIZE = 50;
    public void execute() {

        Set<TypedTuple<String>> top50 = couponRedis.opsForZSet().popMin(COUPON_ISSUE_QUEUE, REQUEST_BATCH_SIZE);
        for (TypedTuple<String> top : top50) {

            String[] ids = top.getValue().split(":");

            long userId = Long.parseLong(ids[0]);
            long couponId = Long.parseLong(ids[1]);

            insertUserCoupon(userId, couponId);
        }
    }

    private void insertUserCoupon(long userId, long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        User user = userRepository.findById(userId);

        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon, user);
        userCouponRepository.save(userCoupon);
    }
}
