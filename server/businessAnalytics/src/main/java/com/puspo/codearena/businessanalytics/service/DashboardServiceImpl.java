package com.puspo.codearena.businessanalytics.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.puspo.codearena.businessanalytics.dto.intermediate.CategoryRevenueShareDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.TopProductDto;
import com.puspo.codearena.businessanalytics.entity.Order;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.repository.OrderAnalyticsRepository;
import com.puspo.codearena.businessanalytics.repository.OrderRepository;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final OrderAnalyticsRepository orderAnalyticsRepository;

    public DashboardServiceImpl(OrderRepository orderRepository, OrderAnalyticsRepository orderAnalyticsRepository) {
        this.orderRepository = orderRepository;
        this.orderAnalyticsRepository = orderAnalyticsRepository;
    }

    @Override
    public DashboardSummaryDto getSummary() {
        return orderRepository.fetchDashboardSummary();
    }

    @Override
    public List<CategorySalesDto> getCategorySales() {
        return orderAnalyticsRepository.fetchCategorySales();
    }

    @Override
    public List<MonthlyRevenueDto> getMonthlyRevenue() {
        return orderRepository.fetchMonthlyRevenue();
    }

    @Override
    public List<Order> getRecentOrders() {
        return orderRepository
                .findAll(
                        Sort.by(
                                Sort.Direction.DESC,
                                "createdAt"
                        )
                )
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    @Override
    public List<Objects[]> getCategoryRevenueShare() {
        return orderRepository.fetchCategoryRevenueShare();
    }
}
