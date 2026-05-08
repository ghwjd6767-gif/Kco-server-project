package com.sparta.kcoserverproject.domain.product.service;

import com.sparta.kcoserverproject.domain.product.dto.ProductResponseDto;
import com.sparta.kcoserverproject.domain.product.entity.Product;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void 커피_메뉴_목록을_조회한다() {

        // given
        Product americano = productRepository.save(
                Product.builder()
                        .name("아메리카노")
                        .price(BigDecimal.valueOf(3000))
                        .build()
        );

        Product latte = productRepository.save(
                Product.builder()
                        .name("라떼")
                        .price(BigDecimal.valueOf(4000))
                        .build()
        );

        // when
        List<ProductResponseDto> result = productService.getProducts();

        // then
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(ProductResponseDto::name)
                .containsExactlyInAnyOrder("아메리카노", "라떼");

        assertThat(result)
                .extracting(ProductResponseDto::price)
                .containsExactlyInAnyOrder(
                        BigDecimal.valueOf(3000),
                        BigDecimal.valueOf(4000)
                );
    }

    @Test
    void 등록된_메뉴가_없으면_빈_목록을_반환한다() {
        // when
        List<ProductResponseDto> result = productService.getProducts();

        // then
        assertThat(result).isEmpty();
    }
}
