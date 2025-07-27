package kr.hhplus.be.server.order.domain.model;

public enum OrderStatus {
    CANCELLED,     // 주문취소
    PENDING,       // 결제 전
    COMPLETED      // 결제완료
}
