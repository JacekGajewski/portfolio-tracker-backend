package com.tracker.portfolio.dto;

import com.tracker.portfolio.enums.SectorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PositionDTO {

    private long id;

    private String ticker;

    private String stockExchange;

    private int amount;

    private BigDecimal value;

    private SectorEnum sector;

    public String getTicker() {
        return ticker.toUpperCase();
    }

    public String getStockExchange() {
        return stockExchange.toUpperCase();
    }
}
