package kr.hhplus.be.server.coupon.exception;

import kr.hhplus.be.server.coupon.domain.model.CouponAction;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import lombok.Getter;

@Getter
public class InvalidCouponStateException extends CouponOperationException {

    private final CouponStatus currentStatus;
    private final CouponAction action;

    public InvalidCouponStateException(CouponStatus currentStatus, CouponAction action) {
        super(buildMessage(currentStatus, action));
        this.currentStatus = currentStatus;
        this.action = action;
    }

    private static String buildMessage(CouponStatus currentStatus, CouponAction action) {
        return String.format(
                "Invalid coupon state transition. currentStatus=%s, action=%s",
                currentStatus,
                action
        );
    }
}