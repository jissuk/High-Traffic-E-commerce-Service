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
import kr.hhplus.be.server.coupon.usecase.reader.CouponReader;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterUserCouponUseCase {

    private final CouponReader couponReader;
    private final UserReader userReader;

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    public void execute(UserCouponCommand command) {

        Coupon coupon = couponReader.findCouponOrThrow(command.couponId());
        User user = userReader.findUserOrThrow(command.userId());

        coupon.decreaseQuantity();

        CouponEntity updateCoupon = couponMapper.toEntity(coupon);

        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon);
        UserCouponEntity saveUserCoupon = userCouponMapper.toEntity(userCoupon);
        saveUserCoupon.setUserId(user.getId());
        saveUserCoupon.setCouponId(coupon.getId());

        couponRepository.save(updateCoupon);
        userCouponRepository.save(saveUserCoupon);
    }

}








