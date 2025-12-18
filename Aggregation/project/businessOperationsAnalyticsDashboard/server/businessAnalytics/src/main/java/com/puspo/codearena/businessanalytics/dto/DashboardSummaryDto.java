package com.puspo.codearena.businessanalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class DashboardSummaryDto {
    private Long totalOrders;
    private Double totalRevenue;
    private Double avgOrderValue;
    private Long completedOrders;
    private Long canceledOrders;
}
