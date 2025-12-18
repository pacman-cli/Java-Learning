package com.pacman.hospital.domain.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDto {
    private Long id;

    private String name;
    private String description;

    private Integer quantityAvailable;
    private BigDecimal price;

    private LocalDate expiryDate;
}
