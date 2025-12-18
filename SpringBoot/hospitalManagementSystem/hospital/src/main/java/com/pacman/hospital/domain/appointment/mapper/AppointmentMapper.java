package com.pacman.hospital.domain.appointment.mapper;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import com.pacman.hospital.domain.appointment.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public static AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setStatus(appointment.getStatus());
        dto.setReason(appointment.getReason());

        //if in the entity, the patient and doctor are not null, then set the id in the dto
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
        }
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
        }

        return dto;
    }

    public static Appointment toEntity(AppointmentDto dto) {
        if (dto == null) return null;

        return Appointment.builder()
                .id(dto.getId())
                .appointmentDateTime(dto.getAppointmentDateTime())
                .status(dto.getStatus())
                .reason(dto.getReason())
                .build();
        // patient and doctor must be set manually in service
    }

    public static void updateEntityFromDto(AppointmentDto dto, Appointment entity) {
        if (dto == null || entity == null) return;
        // don't overwrite id unless explicitly provided (optional)'
        if (dto.getAppointmentDateTime() != null) {
            entity.setAppointmentDateTime(dto.getAppointmentDateTime());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getReason() != null) {
            entity.setReason(dto.getReason());
        }
        // patient and doctor updates must be handled in service
    }
}
