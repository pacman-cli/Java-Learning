package com.portfolio.service;

import com.portfolio.entity.Experience;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.dto.ExperienceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    public List<ExperienceDTO> getAllExperiences() {
        return experienceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExperienceDTO getExperienceById(Long id) {
        return experienceRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ExperienceDTO createExperience(ExperienceDTO experienceDTO) {
        Experience experience = convertToEntity(experienceDTO);
        Experience savedExperience = experienceRepository.save(experience);
        return convertToDTO(savedExperience);
    }

    public ExperienceDTO updateExperience(Long id, ExperienceDTO experienceDTO) {
        return experienceRepository.findById(id)
                .map(existingExperience -> {
                    existingExperience.setJobTitle(experienceDTO.getJobTitle());
                    existingExperience.setCompany(experienceDTO.getCompany());
                    existingExperience.setLocation(experienceDTO.getLocation());
                    existingExperience.setPeriod(experienceDTO.getPeriod());
                    existingExperience.setDescription(experienceDTO.getDescription());
                    existingExperience.setTechnologies(experienceDTO.getTechnologies());
                    Experience updatedExperience = experienceRepository.save(existingExperience);
                    return convertToDTO(updatedExperience);
                })
                .orElse(null);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    private ExperienceDTO convertToDTO(Experience experience) {
        return new ExperienceDTO(
                experience.getId(),
                experience.getJobTitle(),
                experience.getCompany(),
                experience.getLocation(),
                experience.getPeriod(),
                experience.getDescription(),
                experience.getTechnologies());
    }

    private Experience convertToEntity(ExperienceDTO experienceDTO) {
        return new Experience(
                experienceDTO.getJobTitle(),
                experienceDTO.getCompany(),
                experienceDTO.getLocation(),
                experienceDTO.getPeriod(),
                experienceDTO.getDescription(),
                experienceDTO.getTechnologies());
    }
}