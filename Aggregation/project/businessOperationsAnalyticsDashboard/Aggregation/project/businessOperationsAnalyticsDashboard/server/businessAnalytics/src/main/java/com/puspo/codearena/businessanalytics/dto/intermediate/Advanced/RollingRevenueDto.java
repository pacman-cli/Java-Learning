package com.puspo.codearena.businessanalytics.dto.intermediate.Advanced;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RollingRevenueDto {
    private LocalDate date;
    private BigDecimal dailyRevenue;
    private BigDecimal rolling7DayAvg;
}
