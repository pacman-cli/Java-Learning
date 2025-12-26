package com.pacman.hospital.domain.patient.mapper;

import com.pacman.hospital.domain.patient.dto.PatientDto;
import com.pacman.hospital.domain.patient.model.Gender;
import com.pacman.hospital.domain.patient.model.Patient;
import org.springframework.stereotype.Component;

//Trying to use mapper pattern -> mapstruct
//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface PatientMapper {
//    PatientDto toDto(Patient patient);
//    Patient toEntity(PatientDto dto);
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateEntityFromDto(PatientDto dto, @MappingTarget Patient entity);
//}

//manual way is better for understanding
@Component
public class PatientMapper {

    public PatientDto toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        return PatientDto.builder()
            .id(patient.getId())
            .fullName(patient.getFullName())
            .dateOfBirth(patient.getDateOfBirth())
            // Convert enum to String
            .gender(
                patient.getGender() != null ? patient.getGender().name() : null
            )
            .contactInfo(patient.getContactInfo())
            .address(patient.getAddress())
            .emergencyContact(patient.getEmergencyContact())
            .build();
    }

    public Patient toEntity(PatientDto patientDto) {
        if (patientDto == null) {
            return null;
        }
        return Patient.builder()
            .id(patientDto.getId())
            .fullName(patientDto.getFullName())
            .dateOfBirth(patientDto.getDateOfBirth())
            // Convert String to enum
            .gender(
                patientDto.getGender() != null
                    ? Gender.valueOf(patientDto.getGender())
                    : null
            )
            .contactInfo(patientDto.getContactInfo())
            .address(patientDto.getAddress())
            .emergencyContact(patientDto.getEmergencyContact())
            .build();
    }

    public static void updateEntityFromDto(PatientDto dto, Patient entity) {
        if (dto == null || entity == null) return;
        // don't overwrite id unless explicitly provided (optional)
        if (dto.getFullName() != null) entity.setFullName(dto.getFullName());
        if (dto.getDateOfBirth() != null) entity.setDateOfBirth(
            dto.getDateOfBirth()
        );
        if (dto.getGender() != null) // Convert String to enum
        entity.setGender(Gender.valueOf(dto.getGender()));
        if (dto.getContactInfo() != null) entity.setContactInfo(
            dto.getContactInfo()
        );
        if (dto.getAddress() != null) entity.setAddress(dto.getAddress());
        if (dto.getEmergencyContact() != null) entity.setEmergencyContact(
            dto.getEmergencyContact()
        );
    }
}
