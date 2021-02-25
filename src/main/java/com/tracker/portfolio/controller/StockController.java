package com.tracker.portfolio.controller;

import com.tracker.portfolio.dto.StockDTO;
import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("stocks")
@AllArgsConstructor
@CrossOrigin("*")
public class StockController {

    private final StockService stockService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
    public @ResponseBody
    List<Stock> getAll() {
        return stockService.findAll();
    }

    @GetMapping("test/{symbol}")
    public @ResponseBody
    StockDTO getAll(@PathVariable String symbol) {
        return stockService.getStockDTOFromAlphaVantage(symbol);
    }

    @GetMapping("price/{stockExchange}/{ticker}")
    public @ResponseBody
    BigDecimal getPrice(@PathVariable String stockExchange, @PathVariable String ticker) {
        return stockService.getStockPrice(stockExchange.toUpperCase(), ticker.toUpperCase());
    }

}
