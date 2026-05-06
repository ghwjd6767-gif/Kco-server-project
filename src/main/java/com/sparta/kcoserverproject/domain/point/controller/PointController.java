package com.sparta.kcoserverproject.domain.point.controller;

import com.sparta.kcoserverproject.domain.point.dto.PointChargeRequestDto;
import com.sparta.kcoserverproject.domain.point.dto.PointChargeResponseDto;
import com.sparta.kcoserverproject.domain.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/charge")
    public PointChargeResponseDto chargePoint(
            @PathVariable Long userId,
            @Valid @RequestBody PointChargeRequestDto requestDto
            ) {
        return pointService.chargePoint(userId, requestDto);
    }

}
