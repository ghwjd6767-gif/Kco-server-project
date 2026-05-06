package com.sparta.kcoserverproject.domain.point.repository;

import com.sparta.kcoserverproject.domain.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
