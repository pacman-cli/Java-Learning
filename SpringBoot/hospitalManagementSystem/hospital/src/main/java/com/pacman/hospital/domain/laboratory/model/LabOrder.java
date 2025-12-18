package com.pacman.hospital.domain.laboratory.model;

import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabOrder {
    @Id
    private Long id;

    // which test from catalog
    @ManyToOne
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment; // optional: ordered during a visit

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LabOrderStatus status = LabOrderStatus.ORDERED;

    @Column(name = "report_path", length = 2000)
    private String reportPath; // file path / url for stored report

    @Column(length = 1000)
    private String notes;
}
