package com.sparta.kcoserverproject.domain.product.service;

import com.sparta.kcoserverproject.domain.product.dto.ProductResponseDto;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }
}
