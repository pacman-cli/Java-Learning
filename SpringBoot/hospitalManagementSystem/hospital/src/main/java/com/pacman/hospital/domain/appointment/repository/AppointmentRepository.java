package com.pacman.hospital.domain.appointment.repository;

import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //check doctor availability
    List<Appointment> findByDoctorAndAppointmentDateTimeBetween(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);

    List<Appointment> findByDoctorId(Long doctorId);
}
