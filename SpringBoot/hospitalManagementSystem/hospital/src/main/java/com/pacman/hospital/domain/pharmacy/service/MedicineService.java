package com.pacman.hospital.domain.pharmacy.service;

import com.pacman.hospital.domain.pharmacy.dto.MedicineDto;

import java.util.List;

public interface MedicineService {
    MedicineDto createMedicine(MedicineDto medicineDto);

    MedicineDto updateMedicine(Long id, MedicineDto medicineDto);

    void deleteMedicine(Long id);

    MedicineDto getMedicineById(Long id);

    List<MedicineDto> getAllMedicines();
}
