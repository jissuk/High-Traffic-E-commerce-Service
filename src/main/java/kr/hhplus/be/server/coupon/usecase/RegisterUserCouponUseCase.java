package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterUserCouponUseCase {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    public void execute(UserCouponCommand command) {
        CouponEntity couponEntity = findCouponOrThrow(command.couponId());
        UserEntity userEntity = findUserOrThrow(command.userId());

        Coupon coupon = couponMapper.toDomain(couponEntity);
        coupon.decreaseQuantity();

        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon);

        CouponEntity updateCoupon = couponMapper.toEntity(coupon);
        UserCouponEntity saveUserCoupon = userCouponMapper.toEntity(userCoupon);

        saveUserCoupon.setUser(userEntity);
        saveUserCoupon.setCoupon(couponEntity);

        couponRepository.update(updateCoupon);
        userCouponRepository.save(saveUserCoupon);
    }

    private CouponEntity findCouponOrThrow(long id) {
        CouponEntity couponEntity = couponRepository.findById(id);
        if (couponEntity == null) {
            throw new CouponNotFoundException();
        }
        return couponEntity;
    }

    private UserEntity findUserOrThrow(long id) {
        UserEntity userEntity = userRepository.findById(id);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        return userEntity;
    }
}








