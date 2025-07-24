package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.user.domain.repository.UserCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final Map<Long, UserCouponEntity> userCouponTable = new HashMap<>();

    @Override
    public UserCouponEntity findById(long couponId) {
        return userCouponTable.get(couponId);
    }

    @Override
    public UserCouponEntity save(UserCouponEntity userCoupon) {
        long sequence = 0L;
        sequence++;

        return userCouponTable.put(sequence, userCoupon);
    }

}
