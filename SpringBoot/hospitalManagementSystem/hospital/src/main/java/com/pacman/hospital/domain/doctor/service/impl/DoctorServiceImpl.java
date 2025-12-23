package com.pacman.hospital.domain.doctor.service.impl;

import com.pacman.hospital.domain.doctor.dto.DoctorDto;
import com.pacman.hospital.domain.doctor.mapper.DoctorMapper;
import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.doctor.repository.DoctorRepository;
import com.pacman.hospital.domain.doctor.service.DoctorService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        Doctor doctor = doctorMapper.toEntity(doctorDto);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toDto(saved);
    }

    @Override
    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        doctorMapper.updateEntityFromDto(doctorDto, existing);

        Doctor updated = doctorRepository.save(existing);
        return doctorMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return doctorMapper.toDto(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctorRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true) // here q can be full name orspecialization
    public Page<DoctorDto> searchDoctors(String q, Pageable pageable) { // this is used to search for doctors by name or
                                                                        // specialization why we using Pageable is part
                                                                        // of Spring Data’s pagination and sorting
                                                                        // support. and string for q is used to search
                                                                        // for doctors by name or specialization
        if (q == null || q.isBlank()) { // if q is null or blank, return all doctors in the form of dto

            return doctorRepository.findAll(pageable).map(doctorMapper::toDto);
        }
        // otherwise return the doctors that match the search criteria in the form of
        // dto
        return doctorRepository
                .findByFullNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(q, q, pageable)
                .map(doctorMapper::toDto);
    }
}
