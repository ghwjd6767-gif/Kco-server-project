package com.sparta.kcoserverproject.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kcoserverproject.domain.order.controller.OrderController;
import com.sparta.kcoserverproject.domain.order.dto.OrderRequestDto;
import com.sparta.kcoserverproject.domain.order.dto.OrderResponseDto;
import com.sparta.kcoserverproject.domain.order.service.OrderService;
import com.sparta.kcoserverproject.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OrderService orderService;

    @Test
    void 주문에_성공한다() throws Exception {

        OrderRequestDto request =
                new OrderRequestDto(1L, 1L, 1L);

        OrderResponseDto response =
                new OrderResponseDto(
                        1L,
                        1L,
                        1L,
                        1L,
                        3000L,
                        7000L
                );

        when(orderService.order(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void 주문_수량_검증에_실패한다() throws Exception {

        OrderRequestDto request =
                new OrderRequestDto(1L, 1L, 0L);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));
    }
}
