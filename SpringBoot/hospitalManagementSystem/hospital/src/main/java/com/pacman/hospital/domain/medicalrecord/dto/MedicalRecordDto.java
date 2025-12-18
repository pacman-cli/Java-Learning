package com.pacman.hospital.domain.medicalrecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordDto {
    private Long id;

    @NotBlank(message = "Record type is required")
    @Size(max = 200)
    private String recordType;

    @Size(max = 500)
    private String title;

    private String content;

    // filePath optional; if you support file upload, controller will set this
    @Size(max = 2000)
    private String filePath;

    private LocalDateTime createdAt;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}
