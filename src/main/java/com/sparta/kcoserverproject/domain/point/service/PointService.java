package com.sparta.kcoserverproject.domain.point.service;

import com.sparta.kcoserverproject.domain.point.dto.PointChargeRequestDto;
import com.sparta.kcoserverproject.domain.point.dto.PointChargeResponseDto;
import com.sparta.kcoserverproject.domain.point.entity.PointHistory;
import com.sparta.kcoserverproject.domain.point.repository.PointHistoryRepository;
import com.sparta.kcoserverproject.domain.user.entity.User;
import com.sparta.kcoserverproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointChargeResponseDto chargePoint(Long userId, PointChargeRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.chargePoint(requestDto.amount());

        PointHistory pointHistory = PointHistory.charge(user, requestDto.amount());
        pointHistoryRepository.save(pointHistory);

        return new PointChargeResponseDto(
                user.getId(),
                requestDto.amount(),
                user.getPoint()
        );
    }
}
