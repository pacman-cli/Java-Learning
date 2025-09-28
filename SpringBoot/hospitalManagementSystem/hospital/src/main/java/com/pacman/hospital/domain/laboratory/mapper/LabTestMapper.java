package com.pacman.hospital.domain.laboratory.mapper;

import com.pacman.hospital.domain.laboratory.dto.LabTestDto;
import com.pacman.hospital.domain.laboratory.model.LabTest;
import org.springframework.stereotype.Component;

@Component
public class LabTestMapper {
    public LabTestDto toDto(LabTest labTest) {
        if (labTest == null) return null;
        LabTestDto d = new LabTestDto();
        d.setId(labTest.getId());
        d.setTestName(labTest.getTestName());
        d.setDescription(labTest.getDescription());
        d.setCost(labTest.getCost());
        return d;
    }

    public LabTest toEntity(LabTestDto labTestDto) {
        if (labTestDto == null) return null;
        LabTest e = new LabTest();
        e.setId(labTestDto.getId());
        e.setTestName(labTestDto.getTestName());
        e.setDescription(labTestDto.getDescription());
        e.setCost(labTestDto.getCost());
        return e;
    }
}
