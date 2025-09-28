package com.pacman.hospital.domain.laboratory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabOrderDto {
    private Long id;
    private Long labTestId;
    private Long patientId;
    private Long appointmentId;
    private LocalDateTime orderedAt;
    private String status;
    private String reportPath;
    private String notes;
}
