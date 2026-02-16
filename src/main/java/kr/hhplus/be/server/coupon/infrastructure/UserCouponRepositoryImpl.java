package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaUserCouponRepository;

    @Override
    public UserCoupon findById(long couponId) {

        return jpaUserCouponRepository.findById(couponId)
                .orElseThrow(UserCouponNotFoundException::new);
    }

    @Override
    public UserCoupon findByCouponId(long id) {
        return jpaUserCouponRepository.findByCouponId(id)
                .orElseThrow(UserCouponNotFoundException::new);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return jpaUserCouponRepository.save(userCoupon);
    }

}
