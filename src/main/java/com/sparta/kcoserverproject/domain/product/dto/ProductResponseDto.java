package com.sparta.kcoserverproject.domain.product.dto;

import com.sparta.kcoserverproject.domain.product.entity.Product;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        BigDecimal price
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
