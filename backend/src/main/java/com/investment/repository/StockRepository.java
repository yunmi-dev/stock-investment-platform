package com.investment.repository;

import com.investment.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // code로 조회
    Optional<Stock> findByCode(String code);

    // 거래량 기준 상위 10개 (인기 종목)
    List<Stock> findTop10ByOrderByVolumeDesc();
}
