package com.puspo.codearena.businessanalytics.service;

import java.util.List;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategoryRevenueShareDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CustomerLifetimeValueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RegionConversionDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RollingRevenueDto;
import com.puspo.codearena.businessanalytics.entity.Order;

public interface DashboardService {
    DashboardSummaryDto getSummary();

    List<CategorySalesDto> getCategorySales();

    List<MonthlyRevenueDto> getMonthlyRevenue();

    List<Order> getRecentOrders();

    // List<Objects[]> getCategoryRevenueShare();

    List<CategoryRevenueShareDto> getCategoryRevenueShare();

    List<CustomerLifetimeValueDto> getTopCustomers();

    List<RollingRevenueDto> getRolling7DayRevenue();

    List<RegionConversionDto> getConversionRateByRegion();
}
