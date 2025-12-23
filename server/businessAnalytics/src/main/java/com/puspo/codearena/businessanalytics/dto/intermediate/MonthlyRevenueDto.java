package com.puspo.codearena.businessanalytics.dto.intermediate;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyRevenueDto {
    private String month;
    private BigDecimal revenue;

    public MonthlyRevenueDto() {
    }

    // Constructor matching JPQL query
    public MonthlyRevenueDto(String month, BigDecimal revenue) {
        this.month = month;
        this.revenue = revenue;
    }
}
