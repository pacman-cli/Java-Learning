package com.pacman.hospital.domain.appointment.service.impl;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import com.pacman.hospital.domain.appointment.mapper.AppointmentMapper;
import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.appointment.model.AppointmentStatus;
import com.pacman.hospital.domain.appointment.repository.AppointmentRepository;
import com.pacman.hospital.domain.appointment.service.AppointmentService;
import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.doctor.repository.DoctorRepository;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(
        PatientRepository patientRepository,
        DoctorRepository doctorRepository,
        AppointmentRepository appointmentRepository
    ) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        // find patient by id
        Patient patient = patientRepository
            .findById(appointmentDto.getPatientId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " +
                        appointmentDto.getPatientId()
                )
            );
        // find doctor by id
        Doctor doctor = doctorRepository
            .findById(appointmentDto.getDoctorId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Doctor not found with id: " + appointmentDto.getDoctorId()
                )
            );
        // check doctor availability (no overlapping appointments)
        List<Appointment> conflicts =
            appointmentRepository.findByDoctorAndAppointmentDateTimeBetween(
                doctor,
                appointmentDto.getAppointmentDateTime().minusMinutes(1), // here 1 minute means appointment duration
                appointmentDto.getAppointmentDateTime().plusMinutes(30)
            ); // here minutes can be configured
        // dynamically this is before and after buffer time for appointment

        // if list of conflicts is empty, then a doctor is available
        if (!conflicts.isEmpty()) {
            throw new ResourceNotFoundException(
                "Doctor is not available at this time"
            );
        }
        // create appointment
        Appointment appointment = AppointmentMapper.toEntity(appointmentDto);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        // Set additional fields from DTO if provided
        if (appointmentDto.getNotes() != null) {
            appointment.setNotes(appointmentDto.getNotes());
        }

        // save appointment
        Appointment saved = appointmentRepository.save(appointment);

        // return appointment dto
        return AppointmentMapper.toDto(saved);
    }

    @Override
    public AppointmentDto updateAppointment(
        Long id,
        AppointmentDto appointmentDto
    ) {
        // find appointment by id
        Appointment appointment = appointmentRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Appointment not found with id: " + id
                )
            );

        // update appointment
        if (appointmentDto.getPatientId() != null) {
            // if patient id is provided, then update patient
            Patient patient = patientRepository
                .findById(appointmentDto.getPatientId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Patient not found with id: " +
                            appointmentDto.getPatientId()
                    )
                );

            appointment.setPatient(patient); // set patient
        }

        if (appointmentDto.getDoctorId() != null) {
            // if doctor id is provided, then update doctor
            Doctor doctor = doctorRepository
                .findById(appointmentDto.getDoctorId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Doctor not found with id: " +
                            appointmentDto.getDoctorId()
                    )
                );

            appointment.setDoctor(doctor);
        }

        if (appointmentDto.getAppointmentDateTime() != null) {
            appointment.setAppointmentDateTime(
                appointmentDto.getAppointmentDateTime()
            );
        }

        if (appointmentDto.getStatus() != null) appointment.setStatus(
            AppointmentStatus.valueOf(appointmentDto.getStatus())
        );
        if (appointmentDto.getReason() != null) appointment.setReason(
            appointmentDto.getReason()
        );

        if (appointmentDto.getNotes() != null) appointment.setNotes(
            appointmentDto.getNotes()
        );

        if (appointmentDto.getType() != null) appointment.setAppointmentType(
            appointmentDto.getType()
        );

        if (
            appointmentDto.getDuration() != null
        ) appointment.setDurationMinutes(appointmentDto.getDuration());

        if (appointmentDto.getLocation() != null) appointment.setRoomNumber(
            appointmentDto.getLocation()
        );

        // save appointment
        appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Appointment not found with id: " + id
                )
            );

        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment existing = appointmentRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Appointment not found with id: " + id
                )
            );

        appointmentRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository
            .findAllWithPatientAndDoctor()
            .stream()
            .map(AppointmentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDto> searchAppointments(
        String q,
        Pageable pageable
    ) {
        if (q == null || q.isBlank()) {
            return appointmentRepository
                .findAll(pageable)
                .map(AppointmentMapper::toDto);
        }
        return appointmentRepository
            .findByReasonContainingIgnoreCase(q, pageable)
            .map(AppointmentMapper::toDto);
    }

    @Override
    public AppointmentDto updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Appointment not found with id: " + id
                )
            );

        try {
            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(
                status.toUpperCase()
            );
            appointment.setStatus(appointmentStatus);
            appointmentRepository.save(appointment);
            return AppointmentMapper.toDto(appointment);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid status: " +
                    status +
                    ". Valid statuses are: SCHEDULED, CONFIRMED, COMPLETED, CANCELLED"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsByPatient(Long patientId) {
        Patient patient = patientRepository
            .findById(patientId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " + patientId
                )
            );

        return appointmentRepository
            .findByPatient(patient)
            .stream()
            .map(AppointmentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository
            .findById(doctorId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Doctor not found with id: " + doctorId
                )
            );

        return appointmentRepository
            .findByDoctor(doctor)
            .stream()
            .map(AppointmentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsByStatus(String status) {
        try {
            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(
                status.toUpperCase()
            );
            return appointmentRepository
                .findByStatus(appointmentStatus)
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid status: " +
                    status +
                    ". Valid statuses are: SCHEDULED, CONFIRMED, COMPLETED, CANCELLED"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getTodaysAppointments() {
        return appointmentRepository
            .findTodaysAppointments()
            .stream()
            .map(AppointmentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getUpcomingAppointments() {
        return appointmentRepository
            .findUpcomingAppointments()
            .stream()
            .map(AppointmentMapper::toDto)
            .collect(Collectors.toList());
    }
}
