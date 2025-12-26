package com.pacman.hospital.domain.doctor.service;

import com.pacman.hospital.domain.doctor.dto.DoctorDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    DoctorDto createDoctor(DoctorDto doctorDto);

    DoctorDto updateDoctor(Long id, DoctorDto doctorDto);

    DoctorDto getDoctorById(Long id);

    void deleteDoctor(Long id);

    List<DoctorDto> getAllDoctors();

    // this is used to search for doctors by name or specialization
    Page<DoctorDto> searchDoctors(String q, Pageable pageable);

}
