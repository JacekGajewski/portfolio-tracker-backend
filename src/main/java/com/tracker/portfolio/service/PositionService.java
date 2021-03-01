package com.tracker.portfolio.service;

import com.tracker.portfolio.auth.ApplicationUser;
import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.entity.Position;
import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.exception.ForbiddenException;
import com.tracker.portfolio.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final StockService stockService;

    public Position getPosition(long positionId) {
        Optional<Position> positionOptional = positionRepository.findById(positionId);
        if (positionOptional.isPresent()){
            Position position = positionOptional.get();
            checkPositionOwner(position);
            return position;
        }
        return new Position();
    }

    public Portfolio updatePositionInPortfolio(long positionId, PositionDTO positionDTO) {
        Optional<Position> positionOptional = positionRepository.findById(positionId);
        if (positionOptional.isPresent()) {
            Position position = positionOptional.get();
            checkPositionOwner(position);

            int newAmount = positionDTO.getAmount();
            if (newAmount == 0) {
                deletePositionFromPortfolio(position);
            } else {
                updatePositionAmount(position, newAmount);
            }
            return position.getPortfolio();
        }
        return null;
    }

    private void deletePositionFromPortfolio(Position position){
        Portfolio portfolio = position.getPortfolio();
        portfolio.getPositions().remove(position);
        positionRepository.delete(position);
    }

    private void updatePositionAmount(Position position, int newAmount) {
        position.setAmount(newAmount);
        position.setValue(position.getStock().getValue().multiply(BigDecimal.valueOf(newAmount)));
        positionRepository.save(position);
    }


    public void addOrUpdatePosition(Portfolio portfolio, PositionDTO positionDTO) {
        Set<Position> positions = portfolio.getPositions();

        Optional<Position> optionalPosition = positions.stream()
                .filter(p -> p.getStock().getTicker().equals(positionDTO.getTicker()))
                .findFirst();

        if (optionalPosition.isPresent()) {
            Position position = optionalPosition.get();
            addToExistingPosition(position, positionDTO);
        } else {
            Position position = getNewPosition(portfolio, positionDTO);
            positions.add(position);
        }
    }

    private void addToExistingPosition(Position position, PositionDTO positionDTO) {
        position.setAmount(position.getAmount() + positionDTO.getAmount());
        position.setValue(BigDecimal.valueOf(position.getAmount()).multiply(position.getStock().getValue()));
    }

    public Position getNewPosition(Portfolio portfolio, PositionDTO positionDTO) {
        Stock stock = stockService.getStock(positionDTO);
        return new Position(stock, positionDTO.getAmount(),
                positionDTO.getSector(),
                stock.getValue().multiply(BigDecimal.valueOf(positionDTO.getAmount())),
                portfolio);
    }


    private void checkPositionOwner(Position position) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long user_id = applicationUser.getUser_id();
        if(position.getPortfolio().getPortfolioOwner().getId() == user_id) {
            throw new ForbiddenException("User not authorized");
        }
    }
}
