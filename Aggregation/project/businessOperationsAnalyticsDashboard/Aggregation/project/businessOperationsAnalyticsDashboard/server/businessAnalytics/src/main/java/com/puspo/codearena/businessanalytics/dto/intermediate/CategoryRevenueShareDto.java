package com.puspo.codearena.businessanalytics.dto.intermediate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRevenueShareDto {
    private String categoryName;
    private BigDecimal revenueShare;
    private BigDecimal revenueSharePercentage;
}
