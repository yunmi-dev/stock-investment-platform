package com.investment.dto.request;

import com.investment.domain.order.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "주식 ID는 필수입니다")
    private Long stockId;

    @NotNull(message = "주문 유형은 필수입니다")
    private OrderType orderType;

    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다")
    private Integer quantity;

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private BigDecimal price;

    public OrderRequest(Long stockId, OrderType orderType, Integer quantity, BigDecimal price) {
        this.stockId = stockId;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
    }
}
