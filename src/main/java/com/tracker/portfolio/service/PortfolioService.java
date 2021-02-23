package com.tracker.portfolio.service;

import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.entity.Position;
import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PositionService positionService;

    public Portfolio getPortfolio(long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElse(null);
    }

    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio addPosition(long portfolioId, PositionDTO positionDTO) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);

        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = portfolioOptional.get();
            return addPositionToPortfolio(portfolio, positionDTO);
        }
        return null;
    }

    private Portfolio addPositionToPortfolio(Portfolio portfolio, PositionDTO positionDTO) {
        positionService.addOrUpdatePosition(portfolio, positionDTO);
        return portfolioRepository.save(portfolio);
    }


}
