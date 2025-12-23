package com.portfolio.service;

import com.portfolio.entity.Project;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        return projectRepository.findById(id)
                .map(existingProject -> {
                    existingProject.setTitle(projectDTO.getTitle());
                    existingProject.setDescription(projectDTO.getDescription());
                    existingProject.setImageUrl(projectDTO.getImageUrl());
                    existingProject.setTechnologies(projectDTO.getTechnologies());
                    existingProject.setGithubUrl(projectDTO.getGithubUrl());
                    existingProject.setLiveUrl(projectDTO.getLiveUrl());
                    existingProject.setFeatured(projectDTO.getFeatured());
                    Project updatedProject = projectRepository.save(existingProject);
                    return convertToDTO(updatedProject);
                })
                .orElse(null);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectDTO convertToDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getImageUrl(),
                project.getTechnologies(),
                project.getGithubUrl(),
                project.getLiveUrl(),
                project.getFeatured());
    }

    private Project convertToEntity(ProjectDTO projectDTO) {
        return new Project(
                projectDTO.getTitle(),
                projectDTO.getDescription(),
                projectDTO.getImageUrl(),
                projectDTO.getTechnologies(),
                projectDTO.getGithubUrl(),
                projectDTO.getLiveUrl(),
                projectDTO.getFeatured());
    }
}