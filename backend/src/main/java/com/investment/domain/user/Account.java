package com.investment.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 계좌 엔티티
 * 회원의 계좌 정보 (잔고, 총 자산, 수익률)
 */
@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ① 1:1 관계 (User와)
    @OneToOne(fetch = FetchType.LAZY)  // LAZY 로딩 (성능 최적화)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ② 잔고 (현금)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = new BigDecimal("1000000.00");  // 초기 자본 100만원

    // ③ 총 자산 (현금 + 주식 가치)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAssets = new BigDecimal("1000000.00");

    // ④ 총 손익
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalProfitLoss = BigDecimal.ZERO;

    // ⑤ 수익률 (%)
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal profitLossRate = BigDecimal.ZERO;

    // ⑥ 낙관적 락 (동시성 제어)
    @Version
    private Long version;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Account(User user) {
        this.user = user;
        this.updatedAt = LocalDateTime.now();
    }

    // ⑦ 잔고 변경 (매수/매도 시)
    public void updateBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    // ⑧ 총 자산 및 수익률 계산
    public void updateAssets(BigDecimal totalAssets, BigDecimal stockValue) {
        this.totalAssets = totalAssets;
        this.totalProfitLoss = totalAssets.subtract(new BigDecimal("1000000.00"));

        // 수익률 = (총 손익 / 초기 자본) * 100
        BigDecimal initialCapital = new BigDecimal("1000000.00");
        if (initialCapital.compareTo(BigDecimal.ZERO) > 0) {
            this.profitLossRate = this.totalProfitLoss
                    .divide(initialCapital, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.updatedAt = LocalDateTime.now();
    }
}