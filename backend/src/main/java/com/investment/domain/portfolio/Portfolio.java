package com.investment.domain.portfolio;

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
 * 포트폴리오 엔티티
 * 사용자가 현재 보유 중인 주식
 */
@Entity
@Table(name = "portfolios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // 1. 보유 수량
    @Column(nullable = false)
    private Integer quantity = 0;

    // 2. 평균 매입 단가
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal avgPurchasePrice = BigDecimal.ZERO;

    // 3. 총 매입 금액
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;

    // 4. 현재 평가 금액 (현재가 × 수량)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;

    // 5. 평가 손익 (현재 평가 금액 - 총 매입 금액)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal profitLoss = BigDecimal.ZERO;

    // 6. 수익률 (%)
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal profitLossRate = BigDecimal.ZERO;

    @Version
    private Long version;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Portfolio(User user, Stock stock) {
        this.user = user;
        this.stock = stock;
        this.updatedAt = LocalDateTime.now();
    }

    // 7. 주식 매수 (평균 단가 계산)
    public void addStock(Integer quantity, BigDecimal price) {
        BigDecimal purchaseAmount = price.multiply(new BigDecimal(quantity));

        this.totalPurchaseAmount = this.totalPurchaseAmount.add(purchaseAmount);
        this.quantity += quantity;

        // 평균 매입 단가 = 총 매입 금액 / 보유 수량
        if (this.quantity > 0) {
            this.avgPurchasePrice = this.totalPurchaseAmount
                    .divide(new BigDecimal(this.quantity), 2, BigDecimal.ROUND_HALF_UP);
        }

        this.updatedAt = LocalDateTime.now();
    }

    // 8. 주식 매도
    public void removeStock(Integer quantity) {
        this.quantity -= quantity;

        // 전량 매도 시 초기화
        if (this.quantity == 0) {
            this.avgPurchasePrice = BigDecimal.ZERO;
            this.totalPurchaseAmount = BigDecimal.ZERO;
        }

        this.updatedAt = LocalDateTime.now();
    }

    // 9. 현재 평가액 및 손익 계산
    public void updateCurrentValue(BigDecimal currentPrice) {
        this.currentValue = currentPrice.multiply(new BigDecimal(this.quantity));
        this.profitLoss = this.currentValue.subtract(this.totalPurchaseAmount);

        // 수익률 = (평가 손익 / 총 매입 금액) * 100
        if (this.totalPurchaseAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.profitLossRate = this.profitLoss
                    .divide(this.totalPurchaseAmount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.updatedAt = LocalDateTime.now();
    }
}