package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@Transactional
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public void execute(UserCouponCommand command) {

        Coupon coupon = couponRepository.findByIdForUpdate(command.couponId())
                                        .orElseThrow(CouponNotFoundException::new);

        User user = userRepository.findById(command.userId()).orElseThrow(UserNotFoundException::new);

        coupon.checkQuantity();
        coupon.decreaseQuantity();

        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon);
        userCoupon.setCouponId(user.getId());
        userCoupon.setCouponId(coupon.getId());

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);
    }
}
