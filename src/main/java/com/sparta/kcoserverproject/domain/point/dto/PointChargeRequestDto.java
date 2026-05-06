package com.sparta.kcoserverproject.domain.point.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointChargeRequestDto(
        @NotNull(message = "충전 금액은 필수입니다")
        @Min(value = 1, message = "충전 금액은 1원 이상이어야합니다.")
        Long amount
        ) {
}
