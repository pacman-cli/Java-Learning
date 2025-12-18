package com.pacman.hospital.domain.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long id;

    @NotNull(message = "Appointment date time is required")
    @Future(message = "Appointment date time must be in the future")
    private LocalDateTime appointmentDateTime;

    private String status; // e.g., "SCHEDULED", "CANCELLED", "COMPLETED" //it can be enum
    private String reason;

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Doctor id is required")
    private Long doctorId;


}
