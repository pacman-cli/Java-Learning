package com.pacman.hospital.domain.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "patients",
    indexes = {
        @Index(name = "idx_patient_full_name", columnList = "full_name"),
        @Index(name = "idx_patient_contact", columnList = "contact_info"),
        @Index(name = "idx_patient_created_at", columnList = "created_at"),
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Full name is required")
    @Size(
        min = 2,
        max = 100,
        message = "Full name must be between 2 and 100 characters"
    )
    private String fullName;

    @Column(name = "date_of_birth")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "contact_info", length = 20)
    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$",
        message = "Contact info must be a valid phone number"
    )
    private String contactInfo;

    @Column(name = "email", length = 100)
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "address", length = 500)
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Column(name = "emergency_contact", length = 20)
    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$",
        message = "Emergency contact must be a valid phone number"
    )
    private String emergencyContact;

    @Column(name = "emergency_contact_name", length = 100)
    @Size(
        max = 100,
        message = "Emergency contact name cannot exceed 100 characters"
    )
    private String emergencyContactName;

    @Column(name = "emergency_contact_relationship", length = 50)
    @Size(
        max = 50,
        message = "Emergency contact relationship cannot exceed 50 characters"
    )
    private String emergencyContactRelationship;

    @Column(name = "blood_type", length = 10)
    @Pattern(
        regexp = "^(A|B|AB|O)[+-]?$",
        message = "Blood type must be valid (A, B, AB, O with optional + or -)"
    )
    private String bloodType;

    @Column(name = "allergies", length = 1000)
    @Size(
        max = 1000,
        message = "Allergies description cannot exceed 1000 characters"
    )
    private String allergies;

    @Column(name = "medical_history", length = 2000)
    @Size(max = 2000, message = "Medical history cannot exceed 2000 characters")
    private String medicalHistory;

    @Column(name = "insurance_provider", length = 100)
    @Size(
        max = 100,
        message = "Insurance provider name cannot exceed 100 characters"
    )
    private String insuranceProvider;

    @Column(name = "insurance_policy_number", length = 50)
    @Size(
        max = 50,
        message = "Insurance policy number cannot exceed 50 characters"
    )
    private String insurancePolicyNumber;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "notes", length = 1000)
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    // Soft delete
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 50)
    private String deletedBy;

    // Calculated field for age
    @Transient
    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    // Helper method to check if patient is deleted
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

    // Helper method for soft delete
    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isActive = false;
    }

    // Helper method to restore from soft delete
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
        this.isActive = true;
    }
}
