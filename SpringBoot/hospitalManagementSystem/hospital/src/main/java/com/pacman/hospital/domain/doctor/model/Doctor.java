package com.pacman.hospital.domain.doctor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name="specialization", nullable = false)
    private String specialization;

    @Column(name="qualifications")
    private String qualifications;

    @Column(name="contact_info")
    private String contactInfo;

    @Column(name="daily_schedule", length = 2000)
    private String dailySchedule; // could be JSON or structured text
}
