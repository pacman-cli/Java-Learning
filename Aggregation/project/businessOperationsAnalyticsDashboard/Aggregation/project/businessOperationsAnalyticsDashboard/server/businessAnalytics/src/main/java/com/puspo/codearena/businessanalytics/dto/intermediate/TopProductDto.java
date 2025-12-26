package com.puspo.codearena.businessanalytics.dto.intermediate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopProductDto {
    private String productName;
    private Long quantitySold;
}
