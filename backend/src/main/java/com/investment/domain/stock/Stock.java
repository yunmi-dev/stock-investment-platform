package com.investment.domain.stock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 1. 종목 코드 (예: 005930는 삼성전자의 종목 코드)
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // 2. 종목명 (예: 삼성전자)
    @Column(nullable = false, length = 100)
    private String name;

    // 3. 시장 구분 (KOSPI / KOSDAQ 등)
    @Column(nullable = false, length = 20)
    private String market;

    // 4. 활성화 여부 (상장 폐지 등)
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Stock(String code, String name, String market) {
        this.code = code;
        this.name = name;
        this.market = market;
        this.createdAt = LocalDateTime.now();
    }
}