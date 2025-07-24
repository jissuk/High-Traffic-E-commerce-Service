package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final Map<Long, CouponEntity> couponTable = new HashMap<>();

    @Override
    public CouponEntity findById(Long couponId) {
        return couponTable.get(couponId);
    }

    @Override
    public void update(CouponEntity updateCoupon) {
        couponTable.put(updateCoupon.getId(), updateCoupon);
    }

    @Override
    public CouponEntity save(CouponEntity coupon) {
        long sequence = 0L;
        sequence++;
        return couponTable.put(sequence, coupon);
    }

    @Override
    public void clear() {
        couponTable.clear();
    }

}
