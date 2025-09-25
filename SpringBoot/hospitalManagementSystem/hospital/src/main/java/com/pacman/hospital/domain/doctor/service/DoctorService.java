package com.pacman.hospital.domain.doctor.service;

import com.pacman.hospital.domain.doctor.dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    DoctorDto createDoctor(DoctorDto doctorDto);

    DoctorDto updateDoctor(Long id, DoctorDto doctorDto);

    DoctorDto getDoctorById(Long id);

    void deleteDoctor(Long id);

    List<DoctorDto> getAllDoctors();

}
