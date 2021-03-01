package com.tracker.portfolio.mapper;

import com.tracker.portfolio.dto.StockDTO;
import com.tracker.portfolio.dto.TimeSeriesDTO;
import com.tracker.portfolio.entity.Stock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class StockMapper {

    public Stock getStockEntityUSA(StockDTO stockDTO) {
        Map.Entry<String, TimeSeriesDTO> next = stockDTO.getSth().entrySet().iterator().next();
        TimeSeriesDTO value = next.getValue();
        String close = value.getClose();
        return new Stock(
                stockDTO.getStockMetaDataDTO().getSymbol(),
                new BigDecimal(close),
                "USA",
                LocalDate.parse(next.getKey())
        );
    }

}
