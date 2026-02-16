package kr.hhplus.be.server.payment.domain.model;

public enum PaymentStatus {
    PENDING,           // 결제 대기
    REQUESTED,         // 결제 요청
    APPROVED,          // 결제 승인 완료
    CANCELED           // 결제 취소
}

