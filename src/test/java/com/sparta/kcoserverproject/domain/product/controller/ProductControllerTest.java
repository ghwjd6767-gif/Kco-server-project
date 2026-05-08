package com.sparta.kcoserverproject.domain.product.controller;

import com.sparta.kcoserverproject.domain.product.dto.ProductResponseDto;
import com.sparta.kcoserverproject.domain.product.service.ProductService;
import com.sparta.kcoserverproject.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Test
    void 메뉴_목록_조회에_성공한다() throws Exception {

        List<ProductResponseDto> response = List.of(
                new ProductResponseDto(
                        1L,
                        "아메리카노",
                        BigDecimal.valueOf(3000)
                )
        );

        when(productService.getProducts())
                .thenReturn(response);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("아메리카노"));
    }
}
