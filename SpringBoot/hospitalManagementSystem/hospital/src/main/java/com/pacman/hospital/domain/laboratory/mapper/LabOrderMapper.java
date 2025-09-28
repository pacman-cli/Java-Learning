package com.pacman.hospital.domain.laboratory.mapper;

import com.pacman.hospital.domain.laboratory.dto.LabOrderDto;
import com.pacman.hospital.domain.laboratory.model.LabOrder;
import org.springframework.stereotype.Component;

@Component
public class LabOrderMapper {
    public LabOrderDto toDto(LabOrder e) {
        if (e == null) return null;
        LabOrderDto d = new LabOrderDto();
        d.setId(e.getId());
        d.setLabTestId(e.getLabTest() != null ? e.getLabTest().getId() : null);
        d.setPatientId(e.getPatient() != null ? e.getPatient().getId() : null);
        d.setAppointmentId(e.getAppointment() != null ? e.getAppointment().getId() : null);
        d.setOrderedAt(e.getOrderedAt());
        d.setStatus(e.getStatus() != null ? e.getStatus().name() : null);
        d.setReportPath(e.getReportPath());
        d.setNotes(e.getNotes());
        return d;
    }
}
