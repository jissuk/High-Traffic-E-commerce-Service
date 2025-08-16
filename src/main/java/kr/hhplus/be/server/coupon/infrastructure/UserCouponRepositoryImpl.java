package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaUserCouponRepository;
    private final UserCouponMapper  userCouponMapper;

    @Override
    public UserCoupon findById(long couponId) {

        return jpaUserCouponRepository.findById(couponId)
                .map(userCouponMapper::toDomain)
                .orElseThrow(UserCouponNotFoundException::new);
    }

    @Override
    public UserCoupon findByCouponId(long id) {
        return jpaUserCouponRepository.findByCouponId(id)
                .map(userCouponMapper::toDomain)
                .orElseThrow(UserCouponNotFoundException::new);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponMapper.toDomain(
                jpaUserCouponRepository.save(userCouponMapper.toEntity(userCoupon))
        );
    }

}
