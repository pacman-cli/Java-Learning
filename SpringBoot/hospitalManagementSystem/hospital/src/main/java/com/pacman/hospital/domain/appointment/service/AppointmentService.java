package com.pacman.hospital.domain.appointment.service;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto appointmentDto);

    AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto);

    AppointmentDto getAppointmentById(Long id);

    void deleteAppointment(Long id);

    List<AppointmentDto> getAllAppointments();

    // this is used to search for appointments by reason
    Page<AppointmentDto> searchAppointments(String q, Pageable pageable);

    AppointmentDto updateAppointmentStatus(Long id, String status);

    List<AppointmentDto> getAppointmentsByPatient(Long patientId);

    List<AppointmentDto> getAppointmentsByDoctor(Long doctorId);

    List<AppointmentDto> getAppointmentsByStatus(String status);

    List<AppointmentDto> getTodaysAppointments();

    List<AppointmentDto> getUpcomingAppointments();
}
