package com.pacman.hospital.domain.appointment.repository;

import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.appointment.model.AppointmentStatus;
import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.patient.model.Patient;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository
    extends JpaRepository<Appointment, Long> {
    // check doctor availability
    List<Appointment> findByDoctorAndAppointmentDateTimeBetween(
        Doctor doctor,
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    List<Appointment> findByDoctorId(Long doctorId);

    // auto generated query:
    // SELECT *
    // FROM appointments
    // WHERE LOWER(reason) LIKE LOWER('%keyword%')
    // LIMIT 10 OFFSET 0; -- (if pageable = PageRequest.of(0, 10))
    // this is used to search for appointments by reason
    Page<Appointment> findByReasonContainingIgnoreCase(
        String reason,
        Pageable pageable
    );

    // Find appointments by patient with JOIN FETCH to prevent lazy loading
    @Query(
        "SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor WHERE a.patient = :patient ORDER BY a.appointmentDateTime DESC"
    )
    List<Appointment> findByPatient(Patient patient);

    // Find appointments by doctor with JOIN FETCH to prevent lazy loading
    @Query(
        "SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor WHERE a.doctor = :doctor ORDER BY a.appointmentDateTime DESC"
    )
    List<Appointment> findByDoctor(Doctor doctor);

    // Find appointments by status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Find all appointments with patient and doctor eagerly loaded
    @Query(
        "SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor ORDER BY a.appointmentDateTime DESC"
    )
    List<Appointment> findAllWithPatientAndDoctor();

    // Find today's appointments
    @Query(
        "SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor WHERE DATE(a.appointmentDateTime) = CURRENT_DATE ORDER BY a.appointmentDateTime"
    )
    List<Appointment> findTodaysAppointments();

    // Find upcoming appointments (future appointments)
    @Query(
        "SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor WHERE a.appointmentDateTime > CURRENT_TIMESTAMP ORDER BY a.appointmentDateTime"
    )
    List<Appointment> findUpcomingAppointments();
}
