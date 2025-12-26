package com.puspo.codearena.businessanalytics.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategoryRevenueShareDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CustomerLifetimeValueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RegionConversionDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RollingRevenueDto;
import com.puspo.codearena.businessanalytics.entity.Order;
import com.puspo.codearena.businessanalytics.repository.OrderAnalyticsRepository;
import com.puspo.codearena.businessanalytics.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final OrderAnalyticsRepository orderAnalyticsRepository;

//    public DashboardServiceImpl(OrderRepository orderRepository, OrderAnalyticsRepository orderAnalyticsRepository) {
//        this.orderRepository = orderRepository;
//        this.orderAnalyticsRepository = orderAnalyticsRepository;
//    }

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
                                "createdAt"))
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    // @Override
    // public List<Objects[]> getCategoryRevenueShare() {
    // return orderRepository.fetchCategoryRevenueShare();
    // }

    @Override
    public List<CategoryRevenueShareDto> getCategoryRevenueShare() {
        return orderRepository.fetchCategoryRevenueShare()
                .stream()
                .map(o -> new CategoryRevenueShareDto(
                        ((String) o[0]),
                        ((BigDecimal) o[1]),
                        ((BigDecimal) o[2])))
                .toList();
    }

    @Override
    public List<CustomerLifetimeValueDto> getTopCustomers() {
        return orderRepository.fetchTopCustomers()
                .stream()
                .map(o -> new CustomerLifetimeValueDto(
                        ((String) o[0]),
                        ((Number) o[1]).doubleValue(),  //fixed the castings
                        ((Number) o[2]).intValue()))
                .toList();
    }

    @Override
    public List<RollingRevenueDto> getRolling7DayRevenue() {
        return orderRepository.fetchRolling7DayRevenue()
                .stream()
                .map(r -> new RollingRevenueDto(
                        ((Date) r[0]).toLocalDate(),
                        (BigDecimal) r[1],
                        (BigDecimal) r[2]))
                .toList();
    }

    @Override
    public List<RegionConversionDto> getConversionRateByRegion() {
        return orderRepository.fetchConversionRateByRegion()
                .stream()
                .map(r -> new RegionConversionDto(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        ((Number) r[2]).longValue(),
                        (BigDecimal) r[3]))
                .toList();
    }
}
