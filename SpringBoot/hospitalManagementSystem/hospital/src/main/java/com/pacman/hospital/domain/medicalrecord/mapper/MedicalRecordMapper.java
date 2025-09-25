package com.pacman.hospital.domain.medicalrecord.mapper;

import com.pacman.hospital.domain.medicalrecord.dto.MedicalRecordDto;
import com.pacman.hospital.domain.medicalrecord.model.MedicalRecord;

import java.time.LocalDateTime;

//if we use static then we directly can use its those methods without creating object
//we can use component then we have to use @dependency injection in service class to inject
public class MedicalRecordMapper {
    public static MedicalRecordDto toDto(MedicalRecord entity) {
        if (entity == null) return null;
        MedicalRecordDto dto = new MedicalRecordDto();
        dto.setId(entity.getId());
        dto.setRecordType(entity.getRecordType());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setFilePath(entity.getFilePath());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getPatient() != null) {
            dto.setPatientId(entity.getPatient().getId()); // avoid null pointer exception
        }
        return dto;
    }

    public static MedicalRecord toEntity(MedicalRecordDto dto) {
        if (dto == null) return null;
        MedicalRecord entity = new MedicalRecord();
        entity.setRecordType(dto.getRecordType());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setFilePath(dto.getFilePath());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        // patient must be set in service
        return entity;
    }

    public static void updateEntityFromDto(MedicalRecordDto dto, MedicalRecord entity) {
        if (dto.getRecordType() != null) entity.setRecordType(dto.getRecordType());
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getContent() != null) entity.setContent(dto.getContent());
        if (dto.getFilePath() != null) entity.setFilePath(dto.getFilePath());
        if (dto.getCreatedAt() != null) entity.setCreatedAt(dto.getCreatedAt());
        // patient handled in service
    }
}
