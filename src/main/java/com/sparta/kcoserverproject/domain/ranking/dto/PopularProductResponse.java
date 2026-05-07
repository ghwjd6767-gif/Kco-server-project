package com.sparta.kcoserverproject.domain.ranking.dto;

import com.sparta.kcoserverproject.domain.product.entity.Product;

import java.math.BigDecimal;

public record PopularProductResponse(
        Long productId,
        String name,
        BigDecimal price
) {
    public static PopularProductResponse from(Product product) {
        return new PopularProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
