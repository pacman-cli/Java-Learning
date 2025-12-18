package com.pacman.hospital.domain.insurance.dto;

import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceDto {
    
    private Long id;

    private String providerName;

    private String policyNumber;

    private String coverageDetails;

    private LocalDate validFrom;

    private LocalDate validTo;

    private Long patientId;

    private BigDecimal coveragePercent;

}
