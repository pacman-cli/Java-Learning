package com.pacman.hospital.domain.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDto {
    private Long id;

    private String notes;

    private LocalDateTime prescribedAt;

    private Long patientId;

    private Long doctorId;

    private Long medicineId;
}
