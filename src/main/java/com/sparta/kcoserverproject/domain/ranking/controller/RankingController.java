package com.sparta.kcoserverproject.domain.ranking.controller;

import com.sparta.kcoserverproject.domain.ranking.dto.PopularProductResponse;
import com.sparta.kcoserverproject.domain.ranking.service.RankingService;
import com.sparta.kcoserverproject.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/popular")
    public ApiResponse<List<PopularProductResponse>> getPopularProducts() {
        return ApiResponse.success(rankingService.getPopularProducts());
    }
}
