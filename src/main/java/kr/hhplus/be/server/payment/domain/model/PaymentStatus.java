package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.payment.exception.InvalidPaymentStateException;

public enum PaymentStatus {

    PENDING {          // 결제 생성
        @Override
        public PaymentStatus request() {
            return REQUESTED;
        }
    },

    REQUESTED {        // PG 결제 요청
        @Override
        public PaymentStatus approve() {
            return APPROVED;
        }

        @Override
        public PaymentStatus fail() {
            return FAILED;
        }
    },

    APPROVED,          // 결제 완료

    FAILED;            // 결제 실패

    public PaymentStatus request() {
        throw new InvalidPaymentStateException(this, PaymentAction.REQUEST);
    }

    public PaymentStatus approve() {
        throw new InvalidPaymentStateException(this, PaymentAction.APPROVE);
    }

    public PaymentStatus fail() {
        throw new InvalidPaymentStateException(this, PaymentAction.FAIL);
    }
}