package com.pacman.hospital.domain.appointment.controller;

import com.pacman.hospital.core.security.service.UserPrincipal;
import com.pacman.hospital.domain.appointment.dto.AppointmentDto;
import com.pacman.hospital.domain.appointment.service.AppointmentService;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
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

    @GetMapping("/page")
    public ResponseEntity<Page<AppointmentDto>> searchAppointments(
        @RequestParam(value = "q", required = false) String q,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            appointmentService.searchAppointments(q, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(
        @RequestBody AppointmentDto appointmentDto,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        URI location = uriComponentsBuilder
            .path("/api/appointments/{id}")
            .buildAndExpand(appointmentDto.getId())
            .toUri();
        return ResponseEntity.created(location).body(
            appointmentService.createAppointment(appointmentDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(
        @PathVariable Long id,
        @RequestBody AppointmentDto appointmentDto
    ) {
        return ResponseEntity.ok(
            appointmentService.updateAppointment(id, appointmentDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ResponseEntity.ok(
            appointmentService.updateAppointmentStatus(id, status)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            appointmentService.updateAppointmentStatus(id, "CANCELLED")
        );
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<AppointmentDto> confirmAppointment(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            appointmentService.updateAppointmentStatus(id, "CONFIRMED")
        );
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentDto> completeAppointment(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            appointmentService.updateAppointmentStatus(id, "COMPLETED")
        );
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize(
        "hasAnyRole('PATIENT', 'DOCTOR', 'NURSE', 'ADMIN', 'RECEPTIONIST')"
    )
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatient(
        @PathVariable Long patientId
    ) {
        // Security check: Patients can only access their own appointments
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        if (
            authentication != null &&
            authentication.getPrincipal() instanceof UserPrincipal
        ) {
            UserPrincipal userDetails =
                (UserPrincipal) authentication.getPrincipal();

            // If user is a patient, verify they're accessing their own data
            if (
                userDetails
                    .getAuthorities()
                    .stream()
                    .anyMatch(auth ->
                        auth.getAuthority().equals("ROLE_PATIENT")
                    )
            ) {
                if (!userDetails.getId().equals(patientId)) {
                    throw new org.springframework.security.access.AccessDeniedException(
                        "Patients can only access their own appointments"
                    );
                }
            }
        }

        return ResponseEntity.ok(
            appointmentService.getAppointmentsByPatient(patientId)
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctor(
        @PathVariable Long doctorId
    ) {
        return ResponseEntity.ok(
            appointmentService.getAppointmentsByDoctor(doctorId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByStatus(
        @PathVariable String status
    ) {
        return ResponseEntity.ok(
            appointmentService.getAppointmentsByStatus(status)
        );
    }

    @GetMapping("/today")
    public ResponseEntity<List<AppointmentDto>> getTodaysAppointments() {
        return ResponseEntity.ok(appointmentService.getTodaysAppointments());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentDto>> getUpcomingAppointments() {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments());
    }
}
