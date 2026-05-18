package kr.hhplus.be.server.coupon.application.port.out;

import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;

public interface IssueCouponPort {
    void validateDuplicateIssue(UserCouponCommand command);
    void decrementQuantity(UserCouponCommand command);
}
