package com.investment.controller;

import com.investment.dto.request.OrderRequest;
import com.investment.dto.response.ApiResponse;
import com.investment.dto.response.OrderResponse;
import com.investment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody OrderRequest request
    ) {

        OrderResponse response = orderService.createOrder(userId, request);
        return ApiResponse.success(response, "주문이 생성되었습니다");
    }
}
