package com.tracker.portfolio.controller;

import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("stocks")
@AllArgsConstructor
@CrossOrigin("*")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public @ResponseBody
    List<Stock> getAll() {
        return stockService.findAll();
    }
}
