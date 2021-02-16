package com.tracker.portfolio.service;

import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Position;
import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final StockService stockService;

    public Position getPosition(int positionId) {
        return positionRepository.findById(positionId).orElse(null);
    }

    public Position save(PositionDTO positionDTO) {
        Optional<Stock> stockOptional = stockService.findByTicker(positionDTO.getTicker());

        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            Position position = new Position(stock, positionDTO.getAmount(), positionDTO.getSector());
            return positionRepository.save(position);
        }
        return null;
    }
}
