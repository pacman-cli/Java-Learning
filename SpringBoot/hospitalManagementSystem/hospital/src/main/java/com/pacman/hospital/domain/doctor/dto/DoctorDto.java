package com.pacman.hospital.domain.doctor.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 255)
    private String fullName;

    @NotBlank(message = "Specialization is required")
    @Size(max = 500)
    private String specialization;

    @Size(max = 255)
    private String qualifications;

    @Size(max = 255)
    private String contactInfo;

    @Size(max = 2000)
    private String dailySchedule;
}
