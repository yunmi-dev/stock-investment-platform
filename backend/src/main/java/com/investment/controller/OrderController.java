package com.investment.controller;

import com.investment.dto.request.OrderRequest;
import com.investment.dto.response.ApiResponse;
import com.investment.dto.response.OrderResponse;
import com.investment.repository.UserRepository;
import com.investment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderRequest request
    ) {
        Long userId = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"))
                .getId();

        return ApiResponse.success(orderService.createOrder(userId, request), "주문이 생성되었습니다");
    }

    @GetMapping
    public ApiResponse<Page<OrderResponse>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {
        Long userId = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"))
                .getId();

        return ApiResponse.success(orderService.getOrders(userId, pageable));
    }
}
