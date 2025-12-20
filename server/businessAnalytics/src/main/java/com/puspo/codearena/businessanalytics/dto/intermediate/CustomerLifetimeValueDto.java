package com.puspo.codearena.businessanalytics.dto.intermediate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CustomerLifetimeValueDto {
    private String customerName;
    private BigDecimal lifetimeRevenue;
    private Integer rank;
}
