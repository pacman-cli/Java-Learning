package com.pacman.hospital.domain.pharmacy.mapper;


import com.pacman.hospital.domain.pharmacy.dto.MedicineDto;
import com.pacman.hospital.domain.pharmacy.model.Medicine;

public class MedicineMapper {
    public static Medicine toEntity(MedicineDto medicineDto) {
        if (medicineDto == null) return null;
        return Medicine.builder()
                .id(medicineDto.getId())
                .name(medicineDto.getName())
                .description(medicineDto.getDescription())
                .quantityAvailable(medicineDto.getQuantityAvailable())
                .price(medicineDto.getPrice())
                .expiryDate(medicineDto.getExpiryDate())
                .build();
    }

    public static MedicineDto toDto(Medicine medicine) {
        if (medicine == null) return null;
        return MedicineDto.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .quantityAvailable(medicine.getQuantityAvailable())
                .price(medicine.getPrice())
                .expiryDate(medicine.getExpiryDate())
                .build();
    }
}
