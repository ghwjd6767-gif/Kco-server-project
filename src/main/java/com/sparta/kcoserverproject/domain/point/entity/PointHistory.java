package com.sparta.kcoserverproject.domain.point.entity;

import com.sparta.kcoserverproject.domain.point.enums.PointHistoryType;
import com.sparta.kcoserverproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "point_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointHistoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createAt;

    private PointHistory(User user, Long amount, PointHistoryType type) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.createAt = LocalDateTime.now();
    }

    public static  PointHistory charge(User user, Long amount) {
        return new PointHistory(user, amount, PointHistoryType.CHARGE);
    }
}
