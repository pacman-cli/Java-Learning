package com.pacman.hospital.domain.document.model;

import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.doctor.model.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "medical_documents",
    indexes = {
        @Index(name = "idx_document_type", columnList = "document_type"),
        @Index(name = "idx_document_patient", columnList = "patient_id"),
        @Index(name = "idx_document_doctor", columnList = "doctor_id"),
        @Index(name = "idx_document_created_at", columnList = "created_at"),
        @Index(name = "idx_document_file_name", columnList = "file_name")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    private String fileName;

    @Column(name = "original_file_name", nullable = false, length = 255)
    @NotBlank(message = "Original file name is required")
    @Size(max = 255, message = "Original file name cannot exceed 255 characters")
    private String originalFileName;

    @Column(name = "file_path", nullable = false, length = 500)
    @NotBlank(message = "File path is required")
    @Size(max = 500, message = "File path cannot exceed 500 characters")
    private String filePath;

    @Column(name = "file_size", nullable = false)
    @NotNull(message = "File size is required")
    @Min(value = 1, message = "File size must be greater than 0")
    private Long fileSize;

    @Column(name = "content_type", nullable = false, length = 100)
    @NotBlank(message = "Content type is required")
    @Size(max = 100, message = "Content type cannot exceed 100 characters")
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @Column(name = "title", length = 200)
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @Column(name = "description", length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(name = "document_date")
    private LocalDateTime documentDate;

    @Column(name = "is_confidential", nullable = false)
    @Builder.Default
    private Boolean isConfidential = false;

    @Column(name = "access_level", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccessLevel accessLevel = AccessLevel.PRIVATE;

    @Column(name = "tags", length = 500)
    @Size(max = 500, message = "Tags cannot exceed 500 characters")
    private String tags; // Comma-separated tags

    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "checksum", length = 64)
    @Size(max = 64, message = "Checksum cannot exceed 64 characters")
    private String checksum; // For file integrity verification

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_appointment_id")
    private com.pacman.hospital.domain.appointment.model.Appointment relatedAppointment;

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

    // Enums
    public enum DocumentType {
        MEDICAL_REPORT("Medical Report"),
        LAB_RESULT("Laboratory Result"),
        X_RAY("X-Ray"),
        MRI_SCAN("MRI Scan"),
        CT_SCAN("CT Scan"),
        ULTRASOUND("Ultrasound"),
        PRESCRIPTION("Prescription"),
        DISCHARGE_SUMMARY("Discharge Summary"),
        INSURANCE_DOCUMENT("Insurance Document"),
        CONSENT_FORM("Consent Form"),
        VACCINATION_RECORD("Vaccination Record"),
        SURGICAL_REPORT("Surgical Report"),
        PATHOLOGY_REPORT("Pathology Report"),
        CARDIOLOGY_REPORT("Cardiology Report"),
        BLOOD_TEST("Blood Test"),
        URINE_TEST("Urine Test"),
        REFERRAL_LETTER("Referral Letter"),
        MEDICAL_CERTIFICATE("Medical Certificate"),
        OTHER("Other");

        private final String displayName;

        DocumentType(String displayName) {
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

    public enum AccessLevel {
        PUBLIC("Public"),
        PRIVATE("Private"),
        RESTRICTED("Restricted"),
        CONFIDENTIAL("Confidential");

        private final String displayName;

        AccessLevel(String displayName) {
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

    // Helper methods
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

    @Transient
    public String getFileSizeFormatted() {
        if (fileSize == null) return "0 B";

        long size = fileSize;
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", (double) size, units[unitIndex]);
    }

    @Transient
    public String getFileExtension() {
        if (originalFileName == null) return "";
        int lastDot = originalFileName.lastIndexOf('.');
        return lastDot > 0 ? originalFileName.substring(lastDot + 1).toLowerCase() : "";
    }

    @Transient
    public boolean isImage() {
        String extension = getFileExtension();
        return extension.matches("(jpg|jpeg|png|gif|bmp|webp)");
    }

    @Transient
    public boolean isPdf() {
        return "pdf".equalsIgnoreCase(getFileExtension());
    }

    @Transient
    public boolean isOfficeDocument() {
        String extension = getFileExtension();
        return extension.matches("(doc|docx|xls|xlsx|ppt|pptx)");
    }

    // Helper method for soft delete
    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    // Helper method to restore from soft delete
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
    }

    // Helper method to increment version
    public void incrementVersion() {
        this.version = (this.version == null ? 1 : this.version) + 1;
    }

    // Helper method to check if document is recent (uploaded within last 7 days)
    @Transient
    public boolean isRecent() {
        return createdAt != null &&
               createdAt.isAfter(LocalDateTime.now().minusDays(7));
    }

    // Helper method to get display title
    @Transient
    public String getDisplayTitle() {
        return title != null && !title.trim().isEmpty()
               ? title
               : originalFileName;
    }
}
