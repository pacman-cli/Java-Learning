package com.pacman.hospital.domain.medicalrecord.model;

import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "Consultation note", "X-Ray result", "Prescription"
    @Column(name = "record_type", length = 200, nullable = false)
    private String recordType;

    @Column(name = "title", length = 500)
    private String title;

    // long text for notes, diagnosis, prescriptions etc.
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // optional file reference (URL or relative storage path)
    @Column(name = "file_path", length = 2000)
    private String filePath;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}
