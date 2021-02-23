package com.tracker.portfolio.repository;

import com.tracker.portfolio.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

}
