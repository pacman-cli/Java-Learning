package com.puspo.codearena.businessanalytics.dto.intermediate.Advanced;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardAdvanceDto {
    private List<RollingRevenueDto> rollingRevenue;
    private List<RegionConversionDto> conversionRateByRegion;

}
