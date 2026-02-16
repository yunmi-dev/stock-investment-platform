package com.investment.domain.stock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 주식 종목 엔티티
 * 삼성전자, 카카오 등 실제 종목 정보
 */
@Entity
@Table(name = "stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. 종목 코드 (예: 005930은 삼성전자의 종목 코드)
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // 2. 종목명 (예: 삼성전자)
    @Column(nullable = false, length = 100)
    private String name;

    // 3. 시장 구분 (KOSPI / KOSDAQ 등)
    @Column(nullable = false, length = 20)
    private String market;

    // 4. 현재가
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentPrice;

    // 5. 전일 종가
    @Column(precision = 15, scale = 2)
    private BigDecimal previousClose;

    // 6. 거래량
    @Column(nullable = false)
    private Long volume = 0L;

    // 7. 시가총액 (단위: 억원)
    @Column(precision = 20, scale = 2)
    private BigDecimal marketCap;

    // 8. 활성화 여부 (상장 폐지 등)
    @Column(nullable = false)
    private Boolean isActive = true;

    // 9. 마지막 업데이트 시각
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Stock(String code, String name, String market, BigDecimal currentPrice,
                 BigDecimal previousClose, Long volume, BigDecimal marketCap) {
        this.code = code;
        this.name = name;
        this.market = market;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.volume = volume != null ? volume : 0L;
        this.marketCap = marketCap;
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * 주식 가격 업데이트
     */
    public void updatePrice(BigDecimal newPrice) {
        this.previousClose = this.currentPrice;
        this.currentPrice = newPrice;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * 거래량 업데이트
     */
    public void updateVolume(Long volume) {
        this.volume = volume;
        this.lastUpdated = LocalDateTime.now();
    }
}
