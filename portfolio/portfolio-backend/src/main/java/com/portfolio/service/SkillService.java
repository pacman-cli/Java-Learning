package com.portfolio.service;

import com.portfolio.entity.Skill;
import com.portfolio.repository.SkillRepository;
import com.portfolio.dto.SkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SkillDTO> getSkillsByCategory(String category) {
        return skillRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SkillDTO getSkillById(Long id) {
        return skillRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public SkillDTO createSkill(SkillDTO skillDTO) {
        Skill skill = convertToEntity(skillDTO);
        Skill savedSkill = skillRepository.save(skill);
        return convertToDTO(savedSkill);
    }

    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        return skillRepository.findById(id)
                .map(existingSkill -> {
                    existingSkill.setName(skillDTO.getName());
                    existingSkill.setLevel(skillDTO.getLevel());
                    existingSkill.setCategory(skillDTO.getCategory());
                    existingSkill.setIcon(skillDTO.getIcon());
                    Skill updatedSkill = skillRepository.save(existingSkill);
                    return convertToDTO(updatedSkill);
                })
                .orElse(null);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    private SkillDTO convertToDTO(Skill skill) {
        return new SkillDTO(
                skill.getId(),
                skill.getName(),
                skill.getLevel(),
                skill.getCategory(),
                skill.getIcon());
    }

    private Skill convertToEntity(SkillDTO skillDTO) {
        return new Skill(
                skillDTO.getName(),
                skillDTO.getLevel(),
                skillDTO.getCategory(),
                skillDTO.getIcon());
    }
}