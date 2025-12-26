package com.portfolio.web;

import com.portfolio.model.*;
import com.portfolio.repo.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class PortfolioController {
    private final SkillRepository skillRepo;
    private final ProjectRepository projectRepo;
    private final EducationRepository educationRepo;
    private final ExperienceRepository experienceRepo;
    private final ContactMessageRepository contactRepo;

    public PortfolioController(SkillRepository skillRepo, ProjectRepository projectRepo,
                               EducationRepository educationRepo, ExperienceRepository experienceRepo,
                               ContactMessageRepository contactRepo) {
        this.skillRepo = skillRepo;
        this.projectRepo = projectRepo;
        this.educationRepo = educationRepo;
        this.experienceRepo = experienceRepo;
        this.contactRepo = contactRepo;
    }

    @GetMapping("/skills")
    public List<Skill> skills() { return skillRepo.findAll(); }

    @GetMapping("/projects")
    public List<Project> projects() { return projectRepo.findAll(); }

    @GetMapping("/education")
    public List<Education> education() { return educationRepo.findAll(); }

    @GetMapping("/experience")
    public List<Experience> experience() { return experienceRepo.findAll(); }

    @PostMapping("/contact")
    public ResponseEntity<ContactMessage> contact(@Valid @RequestBody ContactMessage msg) {
        return ResponseEntity.ok(contactRepo.save(msg));
    }
}
