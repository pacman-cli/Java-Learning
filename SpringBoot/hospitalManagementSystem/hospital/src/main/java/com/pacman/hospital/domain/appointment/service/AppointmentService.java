package com.pacman.hospital.domain.appointment.service;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto appointmentDto);

    AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto);

    AppointmentDto getAppointmentById(Long id);

    void deleteAppointment(Long id);

    List<AppointmentDto> getAllAppointments();
}
