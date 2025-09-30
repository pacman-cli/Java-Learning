package com.pacman.hospital.domain.appointment.controller;

import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import com.pacman.hospital.domain.appointment.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(
            @RequestBody AppointmentDto appointmentDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        URI location = uriComponentsBuilder.path("/api/appointments/{id}").buildAndExpand(appointmentDto.getId()).toUri();
        return ResponseEntity.created(location).body(appointmentService.createAppointment(appointmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentDto appointmentDto
    ) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
