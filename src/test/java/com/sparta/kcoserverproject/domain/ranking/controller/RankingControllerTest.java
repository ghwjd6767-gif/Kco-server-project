package com.sparta.kcoserverproject.domain.ranking.controller;

import com.sparta.kcoserverproject.domain.ranking.dto.PopularProductResponse;
import com.sparta.kcoserverproject.domain.ranking.service.RankingService;
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

@WebMvcTest(RankingController.class)
@Import(GlobalExceptionHandler.class)
public class RankingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RankingService rankingService;

    @Test
    void 인기메뉴_조회에_성공한다() throws Exception {

        List<PopularProductResponse> response = List.of(
                new PopularProductResponse(
                        1L,
                        "아메리카노",
                        BigDecimal.valueOf(3000)
                )
        );

        when(rankingService.getPopularProducts())
                .thenReturn(response);

        mockMvc.perform(get("/api/products/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name")
                        .value("아메리카노"));
    }
}
