package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.reader.CouponReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CheckCouponStockUseCase {

    private final CouponReader couponReader;

    public void execute(UserCouponCommand command) {
        Coupon coupon = couponReader.findCouponOrThrow(command.couponId());
        coupon.checkQuantity();
    }
}