package com.investment.dto.response;

import com.investment.domain.order.Order;
import com.investment.domain.order.OrderStatus;
import com.investment.domain.order.OrderType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class OrderResponse {

    private Long id;
    private Long userId;
    private Long stockId;
    private String stockName;
    private OrderType orderType;
    private OrderStatus status;
    private Integer quantity;
    private BigDecimal orderPrice;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime executedAt;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.stockId = order.getStock().getId();
        this.stockName = order.getStock().getName();
        this.orderType = order.getOrderType();
        this.status = order.getStatus();
        this.quantity = order.getQuantity();
        this.orderPrice = order.getOrderPrice();
        this.totalAmount = order.getTotalAmount();
        this.createdAt = order.getCreatedAt();
        this.executedAt = order.getExecutedAt();
    }
}
