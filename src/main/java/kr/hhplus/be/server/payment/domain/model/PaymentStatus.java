package kr.hhplus.be.server.payment.domain.model;

public enum PaymentStatus {
    BEFORE_PAYMENT,    // 결제 전
    APPROVED,          // 결제 승인 완료
    CANCELED           // 결제 취소
}

