package com.puspo.codearena.businessanalytics.service.advanced;

import org.springframework.stereotype.Service;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardSummaryService {

    private final OrderRepository orderRepository;

    public DashboardSummaryDto getSummary(
            com.puspo.codearena.businessanalytics.dto.unified.DashboardFiltersDto filters) {

        // For now, we ignore the filter; we’ll apply it later
        return orderRepository.fetchDashboardSummary();
    }
}
