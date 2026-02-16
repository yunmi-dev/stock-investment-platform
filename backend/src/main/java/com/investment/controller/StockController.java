package com.investment.controller;

import com.investment.domain.stock.Stock;
import com.investment.dto.response.ApiResponse;
import com.investment.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * 전체 주식 목록 조회
     * GET /api/stocks
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Stock>>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(ApiResponse.success(stocks));
    }

    /**
     * 특정 주식 조회 (종목 코드)
     * GET /api/stocks/005930
     */
    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<Stock>> getStock(@PathVariable String code) {
        Stock stock = stockService.getStock(code);
        return ResponseEntity.ok(ApiResponse.success(stock));
    }

    /**
     * 인기 종목 조회
     * GET /api/stocks/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Stock>>> getPopularStocks() {
        List<Stock> stocks = stockService.getPopularStocks();
        return ResponseEntity.ok(ApiResponse.success(stocks));
    }
}
