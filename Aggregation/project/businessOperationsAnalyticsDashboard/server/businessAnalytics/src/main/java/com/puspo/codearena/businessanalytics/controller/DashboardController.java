package com.puspo.codearena.businessanalytics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategoryRevenueShareDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CustomerLifetimeValueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.DashboardAdvanceDto;
import com.puspo.codearena.businessanalytics.entity.Order;
import com.puspo.codearena.businessanalytics.service.DashboardService;
import com.puspo.codearena.businessanalytics.service.advanced.UnifiedDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final UnifiedDashboardService unifiedDashboardService;

    @GetMapping("/summary")
    public DashboardSummaryDto getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/category-sales")
    public List<CategorySalesDto> getCategorySales() {
        return dashboardService.getCategorySales();
    }

    @GetMapping("/monthly-revenue")
    public List<MonthlyRevenueDto> getMonthlyRevenue() {
        return dashboardService.getMonthlyRevenue();
    }

    @GetMapping("/orders")
    public List<Order> getRecentOrders() {
        return dashboardService.getRecentOrders();
    }

    // @GetMapping("/category-revenue-share")
    // public List<Objects[]> getCategoryRevenueShare() {
    // return dashboardService.getCategoryRevenueShare();
    // }

    @GetMapping("/category-revenue-share")
    public List<CategoryRevenueShareDto> getCategoryRevenueShare() {
        return dashboardService.getCategoryRevenueShare();
    }

    @GetMapping("/top-customers")
    public List<CustomerLifetimeValueDto> getTopCustomers() {
        return dashboardService.getTopCustomers();
    }

    @GetMapping("/advanced")
    public DashboardAdvanceDto getAdvancedDashboard() {
        return new DashboardAdvanceDto(
                dashboardService.getRolling7DayRevenue(),
                dashboardService.getConversionRateByRegion());
    }

    @PostMapping("/unified")
    public com.puspo.codearena.businessanalytics.dto.unified.DashboardResponseDto getUnifiedDashboard(
            @RequestBody com.puspo.codearena.businessanalytics.dto.unified.DashboardFiltersDto filters) {
        return unifiedDashboardService.getDashboard(filters);
    }
}
