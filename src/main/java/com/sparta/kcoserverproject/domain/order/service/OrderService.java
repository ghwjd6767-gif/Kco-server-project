package com.sparta.kcoserverproject.domain.order.service;

import com.sparta.kcoserverproject.domain.order.dto.OrderRequestDto;
import com.sparta.kcoserverproject.domain.order.dto.OrderResponseDto;
import com.sparta.kcoserverproject.domain.order.entity.Order;
import com.sparta.kcoserverproject.domain.order.entity.OrderItem;
import com.sparta.kcoserverproject.domain.order.repository.OrderItemRepository;
import com.sparta.kcoserverproject.domain.order.repository.OrderRepository;
import com.sparta.kcoserverproject.domain.point.entity.PointHistory;
import com.sparta.kcoserverproject.domain.point.repository.PointHistoryRepository;
import com.sparta.kcoserverproject.domain.product.entity.Product;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import com.sparta.kcoserverproject.domain.user.entity.User;
import com.sparta.kcoserverproject.domain.user.repository.UserRepository;
import com.sparta.kcoserverproject.global.exception.BusinessException;
import com.sparta.kcoserverproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public OrderResponseDto order(OrderRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        Long totalPrice = calculateTotalPrice(product.getPrice(), request.quantity());

        user.usePoint(totalPrice);

        Order order = orderRepository.save(Order.create(user, totalPrice));

        OrderItem orderItem = OrderItem.create(
                order,
                product,
                request.quantity(),
                product.getPrice().longValue()
        );

        orderItemRepository.save(orderItem);

        pointHistoryRepository.save(PointHistory.use(user, totalPrice));

        return new OrderResponseDto(
                order.getId(),
                user.getId(),
                product.getId(),
                request.quantity(),
                totalPrice,
                user.getPoint()
        );
    }

    private Long calculateTotalPrice(BigDecimal price, Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity)).longValue();
    }
}
