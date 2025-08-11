//package kr.hhplus.be.server.coupon.facade;
//
//import kr.hhplus.be.server.coupon.usecase.CheckCouponStockUseCase;
//import kr.hhplus.be.server.coupon.usecase.RegisterUserCouponUseCase;
//import kr.hhplus.be.server.coupon.usecase.RegisterUserCouponUseCase2;
//import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//@RequiredArgsConstructor
//public class CouponFacade {
//
//    private final CheckCouponStockUseCase checkCouponStockUseCase;
//    private final RegisterUserCouponUseCase2 registerUserCouponUseCase;
//
//    @Transactional
//    public void issueCoupon(UserCouponCommand command){
//        checkCouponStockUseCase.execute(command);
//        registerUserCouponUseCase.execute(command);
//    }
//}
