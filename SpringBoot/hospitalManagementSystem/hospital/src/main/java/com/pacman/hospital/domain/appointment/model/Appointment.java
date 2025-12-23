package com.pacman.hospital.domain.appointment.model;

import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "appointments",
    indexes = {
        @Index(
            name = "idx_appointment_datetime",
            columnList = "appointment_datetime"
        ),
        @Index(name = "idx_appointment_status", columnList = "status"),
        @Index(name = "idx_appointment_patient", columnList = "patient_id"),
        @Index(name = "idx_appointment_doctor", columnList = "doctor_id"),
        @Index(name = "idx_appointment_created_at", columnList = "created_at"),
    }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "appointment_datetime", nullable = false)
    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Appointment status is required")
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "reason", length = 500)
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    @Column(name = "notes", length = 1000)
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    @Column(name = "duration_minutes")
    @Min(
        value = 15,
        message = "Appointment duration must be at least 15 minutes"
    )
    @Max(value = 480, message = "Appointment duration cannot exceed 8 hours")
    @Builder.Default
    private Integer durationMinutes = 30;

    @Column(name = "consultation_fee", precision = 8, scale = 2)
    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Consultation fee cannot be negative"
    )
    private BigDecimal consultationFee;

    @Column(name = "is_emergency", nullable = false)
    @Builder.Default
    private Boolean isEmergency = false;

    @Column(name = "appointment_type", length = 50)
    @Size(max = 50, message = "Appointment type cannot exceed 50 characters")
    private String appointmentType; // e.g., "CONSULTATION", "FOLLOW_UP", "SURGERY", "CHECK_UP"

    @Column(name = "room_number", length = 20)
    @Size(max = 20, message = "Room number cannot exceed 20 characters")
    private String roomNumber;

    @Column(name = "symptoms", length = 1000)
    @Size(
        max = 1000,
        message = "Symptoms description cannot exceed 1000 characters"
    )
    private String symptoms;

    @Column(name = "diagnosis", length = 1000)
    @Size(max = 1000, message = "Diagnosis cannot exceed 1000 characters")
    private String diagnosis;

    @Column(name = "prescription", length = 2000)
    @Size(max = 2000, message = "Prescription cannot exceed 2000 characters")
    private String prescription;

    @Column(name = "follow_up_required")
    @Builder.Default
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Column(name = "cancellation_reason", length = 500)
    @Size(
        max = 500,
        message = "Cancellation reason cannot exceed 500 characters"
    )
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancelled_by", length = 50)
    private String cancelledBy;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor is required")
    private Doctor doctor;

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

    // Helper methods
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

    @Transient
    public boolean canBeCancelled() {
        return (
            status == AppointmentStatus.SCHEDULED ||
            status == AppointmentStatus.CONFIRMED
        );
    }

    @Transient
    public boolean canBeRescheduled() {
        return (
            status == AppointmentStatus.SCHEDULED ||
            status == AppointmentStatus.CONFIRMED
        );
    }

    @Transient
    public boolean isUpcoming() {
        return (
            appointmentDateTime != null &&
            appointmentDateTime.isAfter(LocalDateTime.now())
        );
    }

    @Transient
    public boolean isPast() {
        return (
            appointmentDateTime != null &&
            appointmentDateTime.isBefore(LocalDateTime.now())
        );
    }

    @Transient
    public boolean isToday() {
        if (appointmentDateTime == null) return false;
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.toLocalDate().equals(now.toLocalDate());
    }

    @Transient
    public boolean isOverdue() {
        return (
            isPast() &&
            (status == AppointmentStatus.SCHEDULED ||
                status == AppointmentStatus.CONFIRMED)
        );
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

    // Helper method to cancel appointment
    public void cancel(String reason, String cancelledBy) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = LocalDateTime.now();
        this.cancelledBy = cancelledBy;
    }

    // Helper method to complete appointment
    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    // Helper method to check in patient
    public void checkIn() {
        this.status = AppointmentStatus.IN_PROGRESS;
        this.checkedInAt = LocalDateTime.now();
    }

    // Helper method to check out patient
    public void checkOut() {
        this.checkedOutAt = LocalDateTime.now();
        if (this.status == AppointmentStatus.IN_PROGRESS) {
            this.status = AppointmentStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
    }

    // Helper method to reschedule appointment
    public void reschedule(LocalDateTime newDateTime, String updatedBy) {
        this.appointmentDateTime = newDateTime;
        this.status = AppointmentStatus.RESCHEDULED;
        this.updatedBy = updatedBy;
    }

    // Helper method to confirm appointment
    public void confirm() {
        if (this.status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.CONFIRMED;
        }
    }

    // Helper method to mark as no-show
    public void markAsNoShow() {
        this.status = AppointmentStatus.NO_SHOW;
    }

    // Validation: Only require future dates for new scheduled appointments
    @PrePersist
    @PreUpdate
    private void validateAppointmentDateTime() {
        if (
            appointmentDateTime != null && status == AppointmentStatus.SCHEDULED
        ) {
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalStateException(
                    "Scheduled appointments must be for a future date and time"
                );
            }
        }
    }
}
