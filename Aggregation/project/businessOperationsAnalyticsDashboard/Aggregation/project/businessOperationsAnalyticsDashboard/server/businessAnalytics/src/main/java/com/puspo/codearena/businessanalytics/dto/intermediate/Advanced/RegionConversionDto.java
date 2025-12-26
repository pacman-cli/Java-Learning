package com.puspo.codearena.businessanalytics.dto.intermediate.Advanced;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegionConversionDto {
    private String region;
    private Long completedOrders;
    private Long canceledOrders;
    private BigDecimal conversionRate;
}
