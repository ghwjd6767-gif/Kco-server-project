package com.sparta.kcoserverproject.domain.order.dto;

public record OrderResponseDto(
        Long orderId,
        Long userId,
        Long productId,
        Long quantity,
        Long totalPrice,
        Long remainingPoint
) {
}
