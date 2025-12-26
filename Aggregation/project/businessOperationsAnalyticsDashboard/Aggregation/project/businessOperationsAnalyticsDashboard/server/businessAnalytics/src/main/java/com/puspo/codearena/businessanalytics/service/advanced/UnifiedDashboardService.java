package com.puspo.codearena.businessanalytics.service.advanced;

import org.springframework.stereotype.Service;

import com.puspo.codearena.businessanalytics.dto.unified.DashboardFiltersDto;
import com.puspo.codearena.businessanalytics.dto.unified.DashboardResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnifiedDashboardService {
    private final DashboardSummaryService summaryService;
    private final AnalyticsService analyticsService;

    public DashboardResponseDto getDashboard(DashboardFiltersDto filters) {
        return new DashboardResponseDto(
                summaryService.getSummary(filters),
                analyticsService.getMonthlyRevenue(filters),
                analyticsService.getCategorySales(filters),
                analyticsService.getTopProducts(filters),
                analyticsService.getRolling7DayRevenue(filters),
                analyticsService.getConversionRateByRegion(filters),
                analyticsService.getCategoryRevenueShare(filters),
                analyticsService.getTopCustomers(filters));
    }
}
