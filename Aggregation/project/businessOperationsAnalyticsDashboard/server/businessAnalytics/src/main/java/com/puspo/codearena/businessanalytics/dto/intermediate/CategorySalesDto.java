package com.puspo.codearena.businessanalytics.dto.intermediate;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategorySalesDto {
    private String categoryName;
    private BigDecimal totalRevenue;

}
