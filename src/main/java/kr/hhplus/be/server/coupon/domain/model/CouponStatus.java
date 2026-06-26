package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.exception.InvalidCouponStateException;

public enum CouponStatus {

    ISSUED {
        @Override
        public CouponStatus use() {
            return USED;
        }
    },

    USED;

    public CouponStatus use() {
        throw new InvalidCouponStateException(this, CouponAction.USE);
    }
}