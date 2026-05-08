package com.sparta.kcoserverproject.domain.point.service;

import com.sparta.kcoserverproject.domain.point.dto.PointChargeRequestDto;
import com.sparta.kcoserverproject.domain.user.entity.User;
import com.sparta.kcoserverproject.domain.user.repository.UserRepository;
import com.sparta.kcoserverproject.global.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PointServiceTest {

    @Autowired
    PointService pointService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 포인트를_충전할_수_있다() {

        // given
        User user = userRepository.save(
                User.builder()
                        .point(0L)
                        .build()
        );

        PointChargeRequestDto requestDto = new PointChargeRequestDto(10_000L);

        // when
        pointService.chargePoint(user.getId(), requestDto);

        // then
        User result = userRepository.findById(user.getId()).orElseThrow();
        assertThat(result.getPoint()).isEqualTo(10_000L);
    }

    @Test
    void 충전금액이_0원_이하면_예외가_발생한다() {

        // given
        User user = userRepository.save(
                User.builder()
                        .point(0L)
                        .build()
        );

        PointChargeRequestDto requestDto = new PointChargeRequestDto(0L);

        // when & then
        assertThatThrownBy(() -> pointService.chargePoint(user.getId(), requestDto))
                .isInstanceOf(BusinessException.class);
    }


}
