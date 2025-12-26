package com.portfolio.controller;

import com.portfolio.dto.SkillDTO;
import com.portfolio.dto.ProjectDTO;
import com.portfolio.dto.ExperienceDTO;
import com.portfolio.dto.EducationDTO;
import com.portfolio.service.SkillService;
import com.portfolio.service.ProjectService;
import com.portfolio.service.ExperienceService;
import com.portfolio.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private EducationService educationService;

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getAllPortfolioData() {
        Map<String, Object> portfolioData = new HashMap<>();

        List<SkillDTO> skills = skillService.getAllSkills();
        List<ProjectDTO> projects = projectService.getAllProjects();
        List<ExperienceDTO> experiences = experienceService.getAllExperiences();
        List<EducationDTO> educations = educationService.getAllEducations();

        portfolioData.put("skills", skills);
        portfolioData.put("projects", projects);
        portfolioData.put("experiences", experiences);
        portfolioData.put("educations", educations);

        return ResponseEntity.ok(portfolioData);
    }
}