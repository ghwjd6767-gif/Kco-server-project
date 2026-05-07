package com.sparta.kcoserverproject.domain.order.repository;

import com.sparta.kcoserverproject.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
