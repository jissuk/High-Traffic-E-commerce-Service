//package kr.hhplus.be.server.coupon.usecase.reader;
//
//import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
//import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
//import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class UserCouponReader {
//
//    private final UserCouponRepository userCouponRepositor;
//
//    public UserCoupon findByCouponIdUserCouponOrThrow(long couponId){
//        return userCouponRepositor.findByCouponId(couponId).orElseThrow(UserCouponNotFoundException::new);
//    }
//}
