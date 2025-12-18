package com.pacman.hospital.domain.laboratory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "lab_tests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "test_name", nullable = false, length = 255)
    private String testName;

    @Column(length = 1000)
    private String description;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal cost;

}
