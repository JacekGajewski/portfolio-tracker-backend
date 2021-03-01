package com.tracker.portfolio.mapper;

import com.tracker.portfolio.dto.PortfolioDTO;
import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.entity.Position;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PortfolioMapper {

    public PortfolioDTO portfolioEntityToDTO(Portfolio portfolio) {
        Set<Position> positions = portfolio.getPositions();
        Set<PositionDTO> positionDTOSet = new HashSet<>();
        for (Position position : positions) {
            positionDTOSet.add(
                    new PositionDTO(
                            position.getId(),
                            position.getStock().getTicker(),
                            position.getStock().getStockExchange(),
                            position.getAmount(),
                            position.getValue(),
                            position.getSector()
                    )
            );
        }
        return new PortfolioDTO(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getValue(),
                positionDTOSet
        );
    }

}
