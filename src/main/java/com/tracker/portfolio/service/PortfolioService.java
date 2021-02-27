package com.tracker.portfolio.service;

import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.entity.User;
import com.tracker.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PositionService positionService;

    public List<Portfolio> findAll() {
       return portfolioRepository.findAll();
    }

    public Portfolio getPortfolio(long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElse(null);
    }

    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public void createPortfolio(User user) {
        Portfolio portfolio = new Portfolio("New portfolio", BigDecimal.ZERO, new HashSet<>(), user);
        portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio addPosition(long portfolioId, PositionDTO positionDTO) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);

        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = portfolioOptional.get();
            return addPositionToPortfolio(portfolio, positionDTO);
        }
        return null;
    }

    @Transactional
    public Portfolio addPositionToPortfolio(Portfolio portfolio, PositionDTO positionDTO) {
        positionService.addOrUpdatePosition(portfolio, positionDTO);
        return portfolioRepository.save(portfolio);
    }


}
