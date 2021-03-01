package com.tracker.portfolio.service;

import com.tracker.portfolio.auth.ApplicationUser;
import com.tracker.portfolio.dto.PortfolioDTO;
import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.entity.User;
import com.tracker.portfolio.exception.ForbiddenException;
import com.tracker.portfolio.exception.NotUniqueUsernameException;
import com.tracker.portfolio.mapper.PortfolioMapper;
import com.tracker.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PositionService positionService;
    private final PortfolioMapper portfolioMapper;

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    public Portfolio getPortfolio(long portfolioId) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);
        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = portfolioOptional.get();
            checkPortfolioOwner(portfolio);
            return portfolio;
        }
        return portfolioOptional.orElse(null);
    }

    public PortfolioDTO getUserPortfolio(long userId) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findByPortfolioOwnerId(userId);
        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = portfolioOptional.get();
            return portfolioMapper.portfolioEntityToDTO(portfolio);
        }
        return new PortfolioDTO();
    }

    public void createEmptyPortfolio(User user) {
        String portfolioName = user.getUsername() + " portfolio";
        Portfolio portfolio = new Portfolio(portfolioName, BigDecimal.ZERO, new HashSet<>(), user);
        portfolioRepository.save(portfolio);
    }

    @Transactional
    public PortfolioDTO addPosition(long portfolioId, PositionDTO positionDTO) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);
        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = addPositionToPortfolio(portfolioOptional.get(), positionDTO);
            checkPortfolioOwner(portfolio);
            return portfolioMapper.portfolioEntityToDTO(portfolio);
        }
        return null;
    }

    @Transactional
    public Portfolio addPositionToPortfolio(Portfolio portfolio, PositionDTO positionDTO) {
        positionService.addOrUpdatePosition(portfolio, positionDTO);
        return portfolioRepository.save(portfolio);
    }

    private void checkPortfolioOwner(Portfolio portfolio) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long user_id = applicationUser.getUser_id();
        if(portfolio.getPortfolioOwner().getId() == user_id) {
            throw new ForbiddenException("User not authorized");
        }
    }
}
