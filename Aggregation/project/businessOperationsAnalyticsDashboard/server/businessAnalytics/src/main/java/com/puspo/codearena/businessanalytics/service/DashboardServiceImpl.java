package com.puspo.codearena.businessanalytics.service;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService{
    private final OrderRepository orderRepository;

    public DashboardServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public DashboardSummaryDto getSummary() {
        return orderRepository.fetchDashboardSummary();
    }
}
