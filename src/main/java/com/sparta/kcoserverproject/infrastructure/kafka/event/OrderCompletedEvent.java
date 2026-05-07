package com.sparta.kcoserverproject.infrastructure.kafka.event;

public record OrderCompletedEvent(
        Long orderId,
        Long userId,
        Long productId,
        Long quantity,
        Long paymentAmount
) {
}
