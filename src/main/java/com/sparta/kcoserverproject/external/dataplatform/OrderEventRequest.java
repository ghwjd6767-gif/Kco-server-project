package com.sparta.kcoserverproject.external.dataplatform;

public record OrderEventRequest(
        Long userId,
        Long productId,
        Long paymentAmount
) {
}
