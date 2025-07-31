package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaUserCouponRepository;

    public UserCouponRepositoryImpl(JpaUserCouponRepository jpaUserCouponRepository) {
        this.jpaUserCouponRepository = jpaUserCouponRepository;
    }

    @Override
    public UserCouponEntity findById(long couponId) {

        return jpaUserCouponRepository.findById(couponId);
    }

    @Override
    public UserCouponEntity save(UserCouponEntity userCoupon) {
        return jpaUserCouponRepository.save(userCoupon);
    }

    @Override
    public UserCouponEntity findByCouponId(long id) {
        return jpaUserCouponRepository.findByCouponId(id);
    }

}
