package com.sparta.kcoserverproject.domain.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kcoserverproject.domain.point.controller.PointController;
import com.sparta.kcoserverproject.domain.point.dto.PointChargeRequestDto;
import com.sparta.kcoserverproject.domain.point.dto.PointChargeResponseDto;
import com.sparta.kcoserverproject.domain.point.service.PointService;
import com.sparta.kcoserverproject.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PointController.class)
@Import(GlobalExceptionHandler.class)
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PointService pointService;

    @Test
    void 포인트_충전에_성공한다() throws Exception {

        PointChargeRequestDto request =
                new PointChargeRequestDto(10000L);

        PointChargeResponseDto response =
                new PointChargeResponseDto(1L, 10000L, 10000L);

        when(pointService.chargePoint(eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/1/points/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void 포인트_충전_검증에_실패한다() throws Exception {

        PointChargeRequestDto request =
                new PointChargeRequestDto(0L);

        mockMvc.perform(post("/api/users/1/points/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));
    }
}
