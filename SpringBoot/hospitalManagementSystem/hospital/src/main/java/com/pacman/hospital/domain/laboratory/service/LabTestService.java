package com.pacman.hospital.domain.laboratory.service;

import com.pacman.hospital.domain.laboratory.dto.LabTestDto;

import java.util.List;

public interface LabTestService {
    LabTestDto create(LabTestDto dto);

    LabTestDto update(Long id, LabTestDto dto);

    LabTestDto getById(Long id);

    List<LabTestDto> getAll();

    void delete(Long id);
}
