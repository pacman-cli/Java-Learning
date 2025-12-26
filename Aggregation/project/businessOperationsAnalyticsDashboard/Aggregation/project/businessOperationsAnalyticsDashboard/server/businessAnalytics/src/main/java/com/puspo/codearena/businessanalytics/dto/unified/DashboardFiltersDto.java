package com.puspo.codearena.businessanalytics.dto.unified;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DashboardFiltersDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long regionId;
    private Long categoryId;
}
//Filters:
//Date range
//Region
//Category
