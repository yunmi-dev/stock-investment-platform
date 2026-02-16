package com.investment.service;

import com.investment.domain.stock.Stock;
import com.investment.exception.BusinessException;
import com.investment.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;

    /**
     * 전체 주식 목록 조회
     */
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    /**
     * 특정 주식 조회 (종목 코드로)
     */
    public Stock getStock(String code) {
        return stockRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException("주식을 찾을 수 없습니다: " + code));
    }

    /**
     * 주식 시세 업데이트 (외부 API 연동 시 사용)
     */
    @Transactional
    public Stock updateStockPrice(String code, BigDecimal newPrice) {
        Stock stock = getStock(code);
        stock.updatePrice(newPrice);
        return stock;
    }

    /**
     * 인기 종목 조회 (거래량 기준 상위 10개)
     */
    public List<Stock> getPopularStocks() {
        return stockRepository.findTop10ByOrderByVolumeDesc();
    }
}
