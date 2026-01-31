package com.investment.repository;

import com.investment.domain.order.Order;
import com.investment.domain.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.stock WHERE o.user.id = :userId")
    Page<Order> findOrdersWithStock(@Param("userId") Long userId, Pageable pageable);
}