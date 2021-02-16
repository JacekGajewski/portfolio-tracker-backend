package com.tracker.portfolio.entity;

import com.tracker.portfolio.enums.SectorEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    private Stock stock;

    private int amount;

    @Enumerated(EnumType.STRING)
    private SectorEnum sector;

}
