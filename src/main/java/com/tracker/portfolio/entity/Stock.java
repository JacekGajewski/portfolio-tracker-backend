package com.tracker.portfolio.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock extends BaseEntity{

    private String ticker;

    private BigDecimal value;

    private String stockExchange;

    private LocalDate date;

}
