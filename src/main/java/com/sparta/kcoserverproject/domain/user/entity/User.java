package com.sparta.kcoserverproject.domain.user.entity;

import com.sparta.kcoserverproject.global.exception.BusinessException;
import com.sparta.kcoserverproject.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long point = 0L;

    @Builder
    public User(Long point) {
        this.point = point;
    }

    public void chargePoint(Long amount) {
        if (amount == null || amount <= 0) {
            throw new BusinessException(ErrorCode.POINT_INVALID_AMOUNT);
        }

        this.point += amount;
    }

    public void usePoint(Long amount) {
        if (this.point < amount) {
            throw new BusinessException(ErrorCode.POINT_INSUFFICIENT);
        }

        this.point -= amount;
    }
}
