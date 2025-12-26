package com.portfolio.service;

import com.portfolio.entity.Education;
import com.portfolio.repository.EducationRepository;
import com.portfolio.dto.EducationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    public List<EducationDTO> getAllEducations() {
        return educationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EducationDTO getEducationById(Long id) {
        return educationRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public EducationDTO createEducation(EducationDTO educationDTO) {
        Education education = convertToEntity(educationDTO);
        Education savedEducation = educationRepository.save(education);
        return convertToDTO(savedEducation);
    }

    public EducationDTO updateEducation(Long id, EducationDTO educationDTO) {
        return educationRepository.findById(id)
                .map(existingEducation -> {
                    existingEducation.setDegree(educationDTO.getDegree());
                    existingEducation.setInstitution(educationDTO.getInstitution());
                    existingEducation.setLocation(educationDTO.getLocation());
                    existingEducation.setPeriod(educationDTO.getPeriod());
                    existingEducation.setGpa(educationDTO.getGpa());
                    existingEducation.setDescription(educationDTO.getDescription());
                    existingEducation.setCourses(educationDTO.getCourses());
                    Education updatedEducation = educationRepository.save(existingEducation);
                    return convertToDTO(updatedEducation);
                })
                .orElse(null);
    }

    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    private EducationDTO convertToDTO(Education education) {
        return new EducationDTO(
                education.getId(),
                education.getDegree(),
                education.getInstitution(),
                education.getLocation(),
                education.getPeriod(),
                education.getGpa(),
                education.getDescription(),
                education.getCourses());
    }

    private Education convertToEntity(EducationDTO educationDTO) {
        return new Education(
                educationDTO.getDegree(),
                educationDTO.getInstitution(),
                educationDTO.getLocation(),
                educationDTO.getPeriod(),
                educationDTO.getGpa(),
                educationDTO.getDescription(),
                educationDTO.getCourses());
    }
}