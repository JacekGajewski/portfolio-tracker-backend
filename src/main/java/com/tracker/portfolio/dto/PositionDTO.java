package com.tracker.portfolio.dto;

import com.tracker.portfolio.enums.SectorEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDTO {

    private String ticker;

    private int amount;

    private SectorEnum sector;
}
