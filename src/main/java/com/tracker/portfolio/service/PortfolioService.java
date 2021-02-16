package com.tracker.portfolio.service;

import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.entity.Position;
import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PositionService positionService;
    private final StockService stockService;

    public Portfolio getPortfolio(int portfolioId) {
        return portfolioRepository.findById(portfolioId).orElse(null);
    }

    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio addPosition(long portfolioId, PositionDTO positionDTO) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);

        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = portfolioOptional.get();

            Optional<Stock> stockOptional = stockService.findByTicker(positionDTO.getTicker());

            if (stockOptional.isPresent()) {
                Stock stock = stockOptional.get();
                Position position = new Position(stock, positionDTO.getAmount(), positionDTO.getSector());
                portfolio.getPositions().add(position);
            }
            return portfolioRepository.save(portfolio);
        }
        return null;
    }
}
