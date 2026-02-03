package com.investment.service;

import com.investment.domain.order.Order;
import com.investment.domain.order.OrderType;
import com.investment.domain.portfolio.Portfolio;
import com.investment.domain.stock.Stock;
import com.investment.domain.user.Account;
import com.investment.domain.user.User;
import com.investment.dto.request.OrderRequest;
import com.investment.dto.response.OrderResponse;
import com.investment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));

        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new IllegalArgumentException("종목을 찾을 수 없습니다"));

        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다"));

        if (request.getOrderType() == OrderType.BUY) {
            return processBuyOrder(user, stock, account, request);
        } else {
            return processSellOrder(user, stock, account, request);
        }
    }

    private OrderResponse processBuyOrder(User user, Stock stock, Account account, OrderRequest request) {
        BigDecimal totalAmount = request.getPrice().multiply(new BigDecimal(request.getQuantity()));

        if (account.getBalance().compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException("잔고가 부족합니다");
        }

        Order order = Order.builder()
                .user(user)
                .stock(stock)
                .orderType(request.getOrderType())
                .quantity(request.getQuantity())
                .orderPrice(request.getPrice())
                .build();

        order.complete();
        orderRepository.save(order);

        account.updateBalance(totalAmount.negate());

        Portfolio portfolio = portfolioRepository.findByUserIdAndStockId(user.getId(), stock.getId())
                .orElseGet(() -> {
                    Portfolio newPortfolio = Portfolio.builder()
                            .user(user)
                            .stock(stock)
                            .build();
                    return portfolioRepository.save(newPortfolio);
                });

        portfolio.addStock(request.getQuantity(), request.getPrice());

        return new OrderResponse(order);
    }

    private OrderResponse processSellOrder(User user, Stock stock, Account account, OrderRequest request) {
        Portfolio portfolio = portfolioRepository.findByUserIdAndStockId(user.getId(), stock.getId())
                .orElseThrow(() -> new IllegalArgumentException("보유 주식이 없습니다"));

        if (portfolio.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("보유 수량이 부족합니다");
        }

        Order order = Order.builder()
                .user(user)
                .stock(stock)
                .orderType(request.getOrderType())
                .quantity(request.getQuantity())
                .orderPrice(request.getPrice())
                .build();

        order.complete();
        orderRepository.save(order);

        BigDecimal totalAmount = request.getPrice().multiply(new BigDecimal(request.getQuantity()));
        account.updateBalance(totalAmount);

        portfolio.removeStock(request.getQuantity());

        return new OrderResponse(order);
    }
}
