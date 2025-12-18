package com.pacman.hospital.domain.laboratory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabTestDto {
    private Long id;
    private String testName;
    private String description;
    private BigDecimal cost;
}
