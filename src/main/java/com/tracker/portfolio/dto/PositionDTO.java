package com.tracker.portfolio.dto;

import com.tracker.portfolio.enums.SectorEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PositionDTO {

    private String ticker;

    private String stockExchange;

    private int amount;

    private SectorEnum sector;

    public String getTicker() {
        return ticker.toUpperCase();
    }

    public String getStockExchange() {
        return stockExchange.toUpperCase();
    }
}
