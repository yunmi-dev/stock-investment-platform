package com.investment.domain.order;

import com.investment.domain.stock.Stock;
import com.investment.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 주문 엔티티
 * 사용자의 매수/매도 주문 정보
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ① Many-to-One: 여러 주문 → 한 명의 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ② Many-to-One: 여러 주문 → 한 개의 Stock
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // ③ Enum 타입 (매수/매도)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderType orderType;

    // ④ Enum 타입 (주문 상태)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    // ⑤ 수량
    @Column(nullable = false)
    private Integer quantity;

    // ⑥ 주문 단가
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderPrice;

    // ⑦ 총 금액 (단가 × 수량)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // ⑧ 낙관적 락
    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime executedAt;  // 체결 시간
    private LocalDateTime cancelledAt;  // 취소 시간

    @Builder
    public Order(User user, Stock stock, OrderType orderType,
                 Integer quantity, BigDecimal orderPrice) {
        this.user = user;
        this.stock = stock;
        this.orderType = orderType;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.totalAmount = orderPrice.multiply(new BigDecimal(quantity));  // ⑨ 자동 계산
        this.createdAt = LocalDateTime.now();
    }

    // ⑩ 주문 체결
    public void complete() {
        this.status = OrderStatus.COMPLETED;
        this.executedAt = LocalDateTime.now();
    }

    // ⑪ 주문 취소
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }
}