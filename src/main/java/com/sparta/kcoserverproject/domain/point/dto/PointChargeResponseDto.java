package com.sparta.kcoserverproject.domain.point.dto;

public record PointChargeResponseDto(
        Long userId,
        Long chargeAmount,
        Long currentPoint
) {
}
