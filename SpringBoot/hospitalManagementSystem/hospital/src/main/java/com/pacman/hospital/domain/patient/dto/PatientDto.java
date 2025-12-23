package com.pacman.hospital.domain.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private Long id;

    private Long userId;

    @NotBlank(message = "Full name is required")
    @Size(max = 255)
    private String fullName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String gender;

    @NotBlank(message = "Contact info is required")
    @Size(max = 255)
    private String contactInfo;

    @Size(max = 1000)
    private String address;

    @Size(max = 255)
    private String emergencyContact;
}
