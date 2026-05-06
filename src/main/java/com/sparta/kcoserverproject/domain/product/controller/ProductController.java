package com.sparta.kcoserverproject.domain.product.controller;

import com.sparta.kcoserverproject.domain.product.dto.ProductResponseDto;
import com.sparta.kcoserverproject.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> getProducts() {
        return productService.getProducts();
    }
}
