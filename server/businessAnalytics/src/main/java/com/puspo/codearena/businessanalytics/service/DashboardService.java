package com.puspo.codearena.businessanalytics.service;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.TopProductDto;
import com.puspo.codearena.businessanalytics.entity.Order;

import java.util.List;
import java.util.Objects;

public interface DashboardService {
    DashboardSummaryDto getSummary();

    List<CategorySalesDto> getCategorySales();

    List<MonthlyRevenueDto> getMonthlyRevenue();

    List<Order> getRecentOrders();

    List<Objects[]> getCategoryRevenueShare();
}
