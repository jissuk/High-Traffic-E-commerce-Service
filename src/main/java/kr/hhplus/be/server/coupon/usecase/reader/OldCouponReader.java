//package kr.hhplus.be.server.coupon.usecase.reader;
//
//import kr.hhplus.be.server.coupon.domain.model.Coupon;
//import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
//import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CouponReader {
//
//    private final CouponRepository couponRepository;
//
//    public Coupon findCouponOrThrow(long id){
//        return couponRepository.findById(id).orElseThrow(CouponNotFoundException::new);
//    }
//}
