package com.pacman.hospital.domain.pharmacy.service.impl;

import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.doctor.repository.DoctorRepository;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.domain.pharmacy.dto.PrescriptionDto;
import com.pacman.hospital.domain.pharmacy.mapper.PrescriptionMapper;
import com.pacman.hospital.domain.pharmacy.model.Medicine;
import com.pacman.hospital.domain.pharmacy.model.Prescription;
import com.pacman.hospital.domain.pharmacy.repository.MedicineRepository;
import com.pacman.hospital.domain.pharmacy.repository.PrescriptionRepository;
import com.pacman.hospital.domain.pharmacy.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto) {
        Patient patientExisting = patientRepository.findById(prescriptionDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + prescriptionDto.getPatientId()));
        Doctor doctorExisting = doctorRepository.findById(prescriptionDto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + prescriptionDto.getDoctorId()));
        Medicine medicineExisting = medicineRepository.findById(prescriptionDto.getMedicineId())
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id: " + prescriptionDto.getMedicineId()));

        Prescription prescription = PrescriptionMapper.toEntity(prescriptionDto, doctorExisting, medicineExisting,
                patientExisting);
        return PrescriptionMapper.toDto(prescription);
    }

    @Override
    public PrescriptionDto updatePrescription(Long id, PrescriptionDto prescriptionDto) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with id: " + id));
        Patient patient = patientRepository.findById(prescriptionDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Doctor doctor = doctorRepository.findById(prescriptionDto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + prescriptionDto.getDoctorId()));
        Medicine medicine = medicineRepository.findById(prescriptionDto.getMedicineId())
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id: " + prescriptionDto.getMedicineId()));

        Prescription update = PrescriptionMapper.toEntity(prescriptionDto, doctor, medicine, patient);
        update.setNotes(prescriptionDto.getNotes());
        update.setPrescribedAt(prescriptionDto.getPrescribedAt());
        update.setDoctor(doctor);
        update.setMedicine(medicine);
        update.setPatient(patient);
        return PrescriptionMapper.toDto(prescriptionRepository.save(update));
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }

    @Override
    public PrescriptionDto getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .map(PrescriptionMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with id: " + id));
    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions() {
        return prescriptionRepository.findAll()
                .stream()
                .map(PrescriptionMapper::toDto)
                .collect(Collectors.toList());
    }
}