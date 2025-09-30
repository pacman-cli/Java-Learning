package com.pacman.hospital.domain.pharmacy.service;

import com.pacman.hospital.domain.pharmacy.dto.MedicineDto;
import com.pacman.hospital.domain.pharmacy.dto.PrescriptionDto;

import java.util.List;

public interface PrescriptionService {
    PrescriptionDto createPrescription(PrescriptionDto prescriptionDto);

    PrescriptionDto updatePrescription(Long id, PrescriptionDto prescriptionDto);

    void deletePrescription(Long id);

    PrescriptionDto getPrescriptionById(Long id);

    List<PrescriptionDto> getAllPrescriptions();
}
