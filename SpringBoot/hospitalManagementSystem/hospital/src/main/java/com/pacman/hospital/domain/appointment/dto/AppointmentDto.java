package com.pacman.hospital.domain.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private Long id;

    @NotNull(message = "Appointment date time is required")
    @Future(message = "Appointment date time must be in the future")
    private LocalDateTime appointmentDateTime;

    private String status; // e.g., "SCHEDULED", "CANCELLED", "COMPLETED" //it can be enum
    private String reason;
    private String notes;

    @NotNull(message = "Patient id is required")
    private Long patientId;

    private String patientName; // Added for frontend display

    @NotNull(message = "Doctor id is required")
    private Long doctorId;

    private String doctorName; // Added for frontend display

    // Additional fields for frontend display
    private String type; // e.g., "IN_PERSON", "VIDEO", "PHONE"
    private Integer duration; // Duration in minutes
    private String location; // Location or room number
}
