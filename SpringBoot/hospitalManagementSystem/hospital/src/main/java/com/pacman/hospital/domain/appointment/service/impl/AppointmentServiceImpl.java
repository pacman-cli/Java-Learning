package com.pacman.hospital.domain.appointment.service.impl;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import com.pacman.hospital.domain.appointment.mapper.AppointmentMapper;
import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.appointment.repository.AppointmentRepository;
import com.pacman.hospital.domain.appointment.service.AppointmentService;
import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.doctor.repository.DoctorRepository;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(PatientRepository patientRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        //find patient by id
        Patient patient =
                patientRepository.findById(appointmentDto.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentDto.getPatientId()));
        //find doctor by id
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentDto.getDoctorId()));
        //check doctor availability (no overlapping appointments)
        List<Appointment> conflicts = appointmentRepository.findByDoctorAndAppointmentDateTimeBetween(doctor,
                appointmentDto.getAppointmentDateTime().minusMinutes(1), //here 1 minute means appointment duration
                appointmentDto.getAppointmentDateTime().plusMinutes(30)); //here minutes can be configured
        // dynamically this is before and after buffer time for appointment

        //if list of conflicts is empty, then a doctor is available
        if (!conflicts.isEmpty()) {
            throw new ResourceNotFoundException("Doctor is not available at this time");
        }
        //create appointment
        Appointment appointment = AppointmentMapper.toEntity(appointmentDto);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        //save appointment
        Appointment saved = appointmentRepository.save(appointment);

        //return appointment dto
        return AppointmentMapper.toDto(saved);
    }

    @Override
    public AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto) {
        //find appointment by id
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        //update appointment
        if (appointmentDto.getPatientId() != null) { //if patient id is provided, then update patient
            Patient patient = patientRepository.findById(appointmentDto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentDto.getPatientId()));

            appointment.setPatient(patient); //set patient
        }

        if (appointmentDto.getDoctorId() != null) { //if doctor id is provided, then update doctor
            Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentDto.getDoctorId()));

            appointment.setDoctor(doctor);
        }

        if (appointmentDto.getAppointmentDateTime() != null) {
            appointment.setAppointmentDateTime(appointmentDto.getAppointmentDateTime());
        }

        if (appointmentDto.getStatus() != null) appointment.setStatus(appointmentDto.getStatus());
        if (appointmentDto.getReason() != null) appointment.setReason(appointmentDto.getReason());

        //save appointment
        appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointmentRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
