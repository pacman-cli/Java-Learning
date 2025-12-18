package com.puspo.codearena.businessanalytics.dto.intermediate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class CategorySalesDto {
    private String categoryName;
    private BigDecimal TotalRevenue;
}
