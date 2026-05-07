package com.sparta.kcoserverproject.domain.order.entity;

import com.sparta.kcoserverproject.domain.order.enums.OrderStatus;
import com.sparta.kcoserverproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private LocalDateTime orderedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Order(User user, Long totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.COMPLETED;
        this.orderedAt = LocalDateTime.now();
    }

    public static Order create(User user, Long totalPrice) {
        return new Order(user, totalPrice);
    }
}
