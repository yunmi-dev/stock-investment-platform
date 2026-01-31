package com.investment.domain.order;

/**
 * 주문 상태
 */
public enum Orderstatus {
    PENDING,    // 체결 대기중 (미체결 상태)
    COMPLETED,  // 체결 완료
    CALCELLED   // 주문 취소됨
}
