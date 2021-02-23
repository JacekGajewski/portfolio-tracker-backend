package com.tracker.portfolio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tracker.portfolio.enums.SectorEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Stock stock;

    private int amount;

    @Enumerated(EnumType.STRING)
    private SectorEnum sector;

    private BigDecimal value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Portfolio portfolio;

}
