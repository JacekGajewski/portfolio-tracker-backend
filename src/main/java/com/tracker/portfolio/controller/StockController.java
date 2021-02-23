package com.tracker.portfolio.controller;

import com.tracker.portfolio.dto.StockDTO;
import com.tracker.portfolio.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("stocks")
@AllArgsConstructor
@CrossOrigin("*")
public class StockController {

    private final StockService stockService;

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
