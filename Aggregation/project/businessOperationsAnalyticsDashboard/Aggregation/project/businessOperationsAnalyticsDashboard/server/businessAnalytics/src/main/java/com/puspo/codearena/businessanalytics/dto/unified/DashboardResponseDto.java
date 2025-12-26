package com.puspo.codearena.businessanalytics.dto.unified;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.*;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RegionConversionDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RollingRevenueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DashboardResponseDto {
    private DashboardSummaryDto summary;

    private List<MonthlyRevenueDto> monthlyRevenue;
    private List<CategorySalesDto> categorySales;
    private List<TopProductDto> topProducts;

    private List<RollingRevenueDto> rollingRevenue;
    private List<RegionConversionDto> regionConversion;
    private List<CategoryRevenueShareDto> categoryRevenueShare;
    private List<CustomerLifetimeValueDto> topCustomers;
}
