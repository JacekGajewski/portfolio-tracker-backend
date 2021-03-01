package com.tracker.portfolio.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDTO {

    private long portfolioId;

    private String name;

    private BigDecimal value;

    private Set<PositionDTO> positions;
}
