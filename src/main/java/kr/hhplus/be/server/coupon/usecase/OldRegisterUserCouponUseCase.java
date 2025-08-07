//package kr.hhplus.be.server.coupon.usecase;
//
//import kr.hhplus.be.server.common.annotation.UseCase;
//import kr.hhplus.be.server.coupon.domain.model.Coupon;
//import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
//import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
//import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
//import kr.hhplus.be.server.coupon.usecase.reader.CouponReader;
//import kr.hhplus.be.server.user.domain.model.User;
//import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
//import kr.hhplus.be.server.user.usecase.reader.UserReader;
//import lombok.RequiredArgsConstructor;
//
//@UseCase
//@RequiredArgsConstructor
//public class RegisterUserCouponUseCase2 {
//
//    private final CouponReader couponReader;
//    private final UserReader userReader;
//
//    private final CouponRepository couponRepository;
//    private final UserCouponRepository userCouponRepository;
//
//
//    public void execute(UserCouponCommand command) {
//
//        User user = userReader.findUserOrThrow(command.userId());
//        Coupon coupon = couponReader.findCouponOrThrow(command.couponId());
//        coupon.decreaseQuantity();
//
//        UserCoupon userCoupon = UserCoupon.createBeforeUserCoupon(coupon);
//        userCoupon.setUserId(user.getId());
//        userCoupon.setCouponId(coupon.getId());
//
//        couponRepository.save(coupon);
//        userCouponRepository.save(userCoupon);
//    }
//
//}
//
//
//
//
//
//
//
//
