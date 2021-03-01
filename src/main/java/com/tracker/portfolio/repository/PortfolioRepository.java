package com.tracker.portfolio.repository;

import com.tracker.portfolio.entity.Portfolio;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByPortfolioOwnerId(long userId);

}
