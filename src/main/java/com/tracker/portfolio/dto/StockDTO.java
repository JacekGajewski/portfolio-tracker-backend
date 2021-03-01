package com.tracker.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class StockDTO {

    @JsonProperty("Meta Data")
    private StockMetaDataDTO stockMetaDataDTO;

    @JsonProperty("Time Series (Daily)")
    Map<String, TimeSeriesDTO> sth;

}

