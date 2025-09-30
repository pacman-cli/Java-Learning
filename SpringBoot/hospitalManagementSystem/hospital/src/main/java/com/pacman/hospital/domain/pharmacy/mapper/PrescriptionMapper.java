package com.pacman.hospital.domain.pharmacy.mapper;


import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.pharmacy.dto.PrescriptionDto;
import com.pacman.hospital.domain.pharmacy.model.Medicine;
import com.pacman.hospital.domain.pharmacy.model.Prescription;

public class PrescriptionMapper {
    public static Prescription toEntity(
            PrescriptionDto dto,
            Doctor doctorEntity,
            Medicine medicineEntity,
            Patient patientEntity
    ) {
        if (dto == null) return null;
        return Prescription.builder()
                .id(dto.getId())
                .notes(dto.getNotes())
                .prescribedAt(dto.getPrescribedAt())
                .patient(patientEntity)
                .doctor(doctorEntity)
                .medicine(medicineEntity)
                .build();
    }

    public static PrescriptionDto toDto(Prescription entity) {
        if (entity == null) return null;
        return PrescriptionDto.builder()
                .id(entity.getId())
                .notes(entity.getNotes())
                .prescribedAt(entity.getPrescribedAt())
                .patientId(entity.getPatient().getId())
                .doctorId(entity.getDoctor().getId())
                .medicineId(entity.getMedicine().getId())
                .build();
    }
}
