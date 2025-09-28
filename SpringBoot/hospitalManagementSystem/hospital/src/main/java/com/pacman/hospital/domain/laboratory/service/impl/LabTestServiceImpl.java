package com.pacman.hospital.domain.laboratory.service.impl;

import com.pacman.hospital.domain.laboratory.dto.LabTestDto;
import com.pacman.hospital.domain.laboratory.mapper.LabTestMapper;
import com.pacman.hospital.domain.laboratory.model.LabTest;
import com.pacman.hospital.domain.laboratory.repository.LabTestRepository;
import com.pacman.hospital.domain.laboratory.service.LabTestService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository repo;
    private final LabTestMapper mapper;

    public LabTestServiceImpl(LabTestRepository repo, LabTestMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public LabTestDto create(LabTestDto dto) {
        LabTest e = mapper.toEntity(dto);
        LabTest saved = repo.save(e);
        return mapper.toDto(saved);
    }

    @Override
    public LabTestDto update(Long id, LabTestDto dto) {
        LabTest existing =
                repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("LabTest not found: " + id));
        if (dto.getTestName() != null) existing.setTestName(dto.getTestName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getCost() != null) existing.setCost(dto.getCost());
        LabTest saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabTestDto getById(Long id) {
        return mapper.toDto(
                repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("LabTest not found: " + id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestDto> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}