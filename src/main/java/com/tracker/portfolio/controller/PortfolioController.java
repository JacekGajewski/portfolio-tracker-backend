package com.tracker.portfolio.controller;

import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Portfolio;
import com.tracker.portfolio.service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("portfolio")
@AllArgsConstructor
@CrossOrigin("*")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/{portfolioId}")
    public @ResponseBody
    Portfolio getPortfolio(@PathVariable long portfolioId) {
        return portfolioService.getPortfolio(portfolioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio
    savePortfolio(@RequestBody Portfolio portfolio) {
        return portfolioService.save(portfolio);
    }

    @PostMapping("/{portfolioId}")
    public Portfolio
    addPositionToPortfolio(@PathVariable long portfolioId, @RequestBody PositionDTO positionDTO) {
        return portfolioService.addPosition(portfolioId, positionDTO);
    }



}
