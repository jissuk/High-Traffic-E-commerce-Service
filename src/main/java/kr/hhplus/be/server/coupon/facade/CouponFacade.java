package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.usecase.CheckCouponStockUseCase;
import kr.hhplus.be.server.coupon.usecase.RegisterUserCouponUseCase;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final CheckCouponStockUseCase checkCouponStockUseCase;
    private final RegisterUserCouponUseCase registerUserCouponUseCase;

    public void issueCoupon(UserCouponRequestDTO request){

        checkCouponStockUseCase.execute(request);
        registerUserCouponUseCase.execute(request);
    }
}
