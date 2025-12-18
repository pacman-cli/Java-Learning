package com.pacman.hospital.domain.patient.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="gender")
    private String gender;

    @Column(name="contact_info")
    private String contactInfo;

    @Column(name="address", length = 1000)
    private String address;

    @Column(name="emergency_contact")
    private String emergencyContact;


}
