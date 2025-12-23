package com.pacman.hospital.domain.appointment.mapper;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.appointment.model.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public static AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        // Convert enum to String
        dto.setStatus(
            appointment.getStatus() != null
                ? appointment.getStatus().name()
                : null
        );
        dto.setReason(appointment.getReason());
        dto.setNotes(appointment.getNotes());

        // Set additional fields
        dto.setType(appointment.getAppointmentType());
        dto.setDuration(appointment.getDurationMinutes());
        dto.setLocation(appointment.getRoomNumber());

        //if in the entity, the patient and doctor are not null, then set the id and name in the dto
        try {
            if (appointment.getPatient() != null) {
                dto.setPatientId(appointment.getPatient().getId());
                dto.setPatientName(appointment.getPatient().getFullName());
            }
        } catch (Exception e) {
            // Handle lazy loading exception - just set the ID if available
            dto.setPatientId(null);
            dto.setPatientName(null);
        }

        try {
            if (appointment.getDoctor() != null) {
                dto.setDoctorId(appointment.getDoctor().getId());
                dto.setDoctorName(appointment.getDoctor().getFullName());
            }
        } catch (Exception e) {
            // Handle lazy loading exception - just set the ID if available
            dto.setDoctorId(null);
            dto.setDoctorName(null);
        }

        return dto;
    }

    public static Appointment toEntity(AppointmentDto dto) {
        if (dto == null) return null;

        return Appointment.builder()
            .id(dto.getId())
            .appointmentDateTime(dto.getAppointmentDateTime())
            // Convert String to enum
            .status(
                dto.getStatus() != null
                    ? AppointmentStatus.valueOf(dto.getStatus())
                    : null
            )
            .reason(dto.getReason())
            .appointmentType(dto.getType())
            .durationMinutes(dto.getDuration())
            .roomNumber(dto.getLocation())
            .build();
        // patient and doctor must be set manually in service
    }

    public static void updateEntityFromDto(
        AppointmentDto dto,
        Appointment entity
    ) {
        if (dto == null || entity == null) return;
        // don't overwrite id unless explicitly provided (optional)'
        if (dto.getAppointmentDateTime() != null) {
            entity.setAppointmentDateTime(dto.getAppointmentDateTime());
        }
        if (dto.getStatus() != null) {
            // Convert String to enum
            entity.setStatus(AppointmentStatus.valueOf(dto.getStatus()));
        }
        if (dto.getReason() != null) {
            entity.setReason(dto.getReason());
        }
        if (dto.getType() != null) {
            entity.setAppointmentType(dto.getType());
        }
        if (dto.getDuration() != null) {
            entity.setDurationMinutes(dto.getDuration());
        }
        if (dto.getLocation() != null) {
            entity.setRoomNumber(dto.getLocation());
        }
        // patient and doctor updates must be handled in service
    }
}
