package com.pacman.hospital.domain.insurance.model;

import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insurance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @Column(name = "coverage_details", nullable = false)
    private String coverageDetails;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    // decimal point and scale is the number of digits to the right of the decimal point
    @Column(name = "coverage_percent", precision = 5, scale = 2) //precision is the number of digits after the
    private BigDecimal coveragePercent;
}
