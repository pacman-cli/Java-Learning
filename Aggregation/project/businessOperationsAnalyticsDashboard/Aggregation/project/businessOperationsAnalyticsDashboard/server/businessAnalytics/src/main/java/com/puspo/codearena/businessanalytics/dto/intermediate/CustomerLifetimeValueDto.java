package com.puspo.codearena.businessanalytics.dto.intermediate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerLifetimeValueDto {
    private String customerName;
    private Double lifetimeRevenue;
    private Integer rank;
}
