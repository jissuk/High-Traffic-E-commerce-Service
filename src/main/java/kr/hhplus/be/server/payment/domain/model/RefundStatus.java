package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.payment.exception.InvalidRefundStateException;

public enum RefundStatus {

    NONE {     // 환불 없음
        @Override
        public RefundStatus request() {
            return REQUESTED;
        }
    },

    REQUESTED {    // 환불 요청
        @Override
        public RefundStatus approve() {
            return APPROVED;
        }

        @Override
        public RefundStatus reject() {
            return REJECTED;
        }
    },

    APPROVED {     // 환불 승인
        @Override
        public RefundStatus complete() {
            return COMPLETED;
        }
    },

    COMPLETED,     // 환불 완료

    REJECTED;      // 환불 거절

    public RefundStatus request() {
        throw new InvalidRefundStateException(this, RefundAction.REQUEST);
    }

    public RefundStatus approve() {
        throw new InvalidRefundStateException(this, RefundAction.APPROVE);
    }

    public RefundStatus reject() {
        throw new InvalidRefundStateException(this, RefundAction.REJECT);
    }

    public RefundStatus complete() {
        throw new InvalidRefundStateException(this, RefundAction.COMPLETE);
    }
}