package com.pacman.hospital.domain.doctor.mapper;

import com.pacman.hospital.domain.doctor.dto.DoctorDto;
import com.pacman.hospital.domain.doctor.model.Doctor;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

//
//@Mapper(componentModel = "spring" ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface DoctorMapper {
//    DoctorDto toDto(Doctor doctor);
//    Doctor toEntity(DoctorDto dto);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateEntityFromDto(DoctorDto dto, @MappingTarget Doctor entity);
//}

//the manual way is better for understanding
@Component
public class DoctorMapper {
    public DoctorDto toDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return DoctorDto.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .contactInfo(doctor.getContactInfo())
                .specialization(doctor.getSpecialization())
                .qualifications(doctor.getQualifications())
                .dailySchedule(doctor.getDailySchedule())
                .build();
    }

    public Doctor toEntity(DoctorDto doctorDto) {
        if (doctorDto == null) {
            return null;
        }

        return Doctor.builder()
                .id(doctorDto.getId())
                .fullName(doctorDto.getFullName())
                .contactInfo(doctorDto.getContactInfo())
                .specialization(doctorDto.getSpecialization())
                .qualifications(doctorDto.getQualifications())
                .dailySchedule(doctorDto.getDailySchedule())
                .build();
    }

    public void updateEntityFromDto(DoctorDto dto, Doctor entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getFullName() != null) {
            entity.setFullName(dto.getFullName());
        }
        if (dto.getContactInfo() != null) {
            entity.setContactInfo(dto.getContactInfo());
        }
        if (dto.getSpecialization() != null) {
            entity.setSpecialization(dto.getSpecialization());
        }
        if (dto.getQualifications() != null) {
            entity.setQualifications(dto.getQualifications());
        }
    }
}