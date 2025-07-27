package kr.hhplus.be.server.coupon.domain.service;

import kr.hhplus.be.server.common.annotation.DomainService;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.order.domain.model.OrderItem;

@DomainService
public class UserCouponDomainService {

    public void applyCoupon(OrderItem orderItem, UserCoupon userCoupon) {
        userCoupon.checkCoupon();
        orderItem.deductCouponAmount(userCoupon.getDiscount());
        userCoupon.setCouponStatus(CouponStatus.USED);
    }

    public void withdrawCoupon(OrderItem orderItem, UserCoupon userCoupon) {
        orderItem.withdrawCouponAmount(userCoupon.getDiscount());
        userCoupon.setCouponStatus(CouponStatus.ISSUED);
    }
}
