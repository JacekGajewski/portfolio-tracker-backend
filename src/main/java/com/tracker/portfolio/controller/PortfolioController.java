package com.tracker.portfolio.controller;

import com.tracker.portfolio.dto.PortfolioDTO;
import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("portfolio")
@AllArgsConstructor
@CrossOrigin("*")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/{portfolioId}")
    @ResponseBody
    public Portfolio getPortfolio(@PathVariable long portfolioId) {
        return portfolioService.getPortfolio(portfolioId);
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public PortfolioDTO getUserPortfolio(@PathVariable long userId) {
        return portfolioService.getUserPortfolio(userId);
    }

    @PostMapping("/{portfolioId}")
    public PortfolioDTO addPositionToPortfolio(@PathVariable long portfolioId, @RequestBody PositionDTO positionDTO) {
        return portfolioService.addPosition(portfolioId, positionDTO);
    }
}
