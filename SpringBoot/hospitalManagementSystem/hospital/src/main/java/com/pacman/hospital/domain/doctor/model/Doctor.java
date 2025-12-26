package com.pacman.hospital.domain.doctor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "doctors",
    indexes = {
        @Index(name = "idx_doctor_full_name", columnList = "full_name"),
        @Index(
            name = "idx_doctor_specialization",
            columnList = "specialization"
        ),
        @Index(name = "idx_doctor_department", columnList = "department"),
        @Index(
            name = "idx_doctor_license_number",
            columnList = "license_number"
        ),
        @Index(name = "idx_doctor_created_at", columnList = "created_at"),
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Full name is required")
    @Size(
        min = 2,
        max = 100,
        message = "Full name must be between 2 and 100 characters"
    )
    private String fullName;

    @Column(name = "specialization", nullable = false, length = 100)
    @NotBlank(message = "Specialization is required")
    @Size(
        min = 2,
        max = 100,
        message = "Specialization must be between 2 and 100 characters"
    )
    private String specialization;

    @Column(name = "qualifications", length = 500)
    @Size(max = 500, message = "Qualifications cannot exceed 500 characters")
    private String qualifications;

    @Column(name = "contact_info", length = 20)
    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$",
        message = "Contact info must be a valid phone number"
    )
    private String contactInfo;

    @Column(name = "email", length = 100)
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "license_number", unique = true, length = 50)
    @NotBlank(message = "License number is required")
    @Size(
        min = 5,
        max = 50,
        message = "License number must be between 5 and 50 characters"
    )
    private String licenseNumber;

    @Column(name = "license_expiry_date")
    @Future(message = "License expiry date must be in the future")
    private LocalDate licenseExpiryDate;

    @Column(name = "department", length = 100)
    @Size(max = 100, message = "Department name cannot exceed 100 characters")
    private String department;

    @Column(name = "years_of_experience")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 60, message = "Years of experience cannot exceed 60")
    private Integer yearsOfExperience;

    @Column(name = "date_of_birth")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

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

    @Column(name = "salary", precision = 10, scale = 2)
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Salary must be greater than 0"
    )
    private java.math.BigDecimal salary;

    @Column(name = "consultation_fee", precision = 8, scale = 2)
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Consultation fee must be greater than 0"
    )
    private java.math.BigDecimal consultationFee;

    @Column(name = "daily_schedule", length = 2000)
    @Size(max = 2000, message = "Daily schedule cannot exceed 2000 characters")
    private String dailySchedule; // JSON or structured text

    @ElementCollection
    @CollectionTable(
        name = "doctor_working_days",
        joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Column(name = "working_day")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> workingDays;

    @Column(name = "start_time")
    private java.time.LocalTime startTime;

    @Column(name = "end_time")
    private java.time.LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "bio", length = 1000)
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @Column(name = "languages_spoken", length = 200)
    @Size(max = 200, message = "Languages spoken cannot exceed 200 characters")
    private String languagesSpoken;

    @Column(name = "awards_certifications", length = 1000)
    @Size(
        max = 1000,
        message = "Awards and certifications cannot exceed 1000 characters"
    )
    private String awardsCertifications;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

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

    // Helper method to check if doctor is deleted
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

    // Helper method for soft delete
    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isActive = false;
        this.isAvailable = false;
    }

    // Helper method to restore from soft delete
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
        this.isActive = true;
        this.isAvailable = true;
    }

    // Helper method to check if license is expired
    @Transient
    public boolean isLicenseExpired() {
        return (
            licenseExpiryDate != null &&
            licenseExpiryDate.isBefore(LocalDate.now())
        );
    }

    // Helper method to check if license expires soon (within 30 days)
    @Transient
    public boolean isLicenseExpiringSoon() {
        return (
            licenseExpiryDate != null &&
            licenseExpiryDate.isAfter(LocalDate.now()) &&
            licenseExpiryDate.isBefore(LocalDate.now().plusDays(30))
        );
    }

    // Helper method to get full display name with title
    @Transient
    public String getDisplayName() {
        return "Dr. " + fullName;
    }

    // Working days enum
    public enum DayOfWeek {
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday"),
        SUNDAY("Sunday");

        private final String displayName;

        DayOfWeek(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
