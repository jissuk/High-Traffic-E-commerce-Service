package kr.hhplus.be.server.coupon.application.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class RegisterUserCouponUseCase {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public void execute(UserCouponCommand command){
        Coupon coupon = couponRepository.findById(command.couponId());
        User user = userRepository.findById(command.userId());

        coupon.decreaseQuantity();
        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon, user);

        userCouponRepository.save(userCoupon);
    }
}
