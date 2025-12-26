package com.pacman.hospital.domain.laboratory.controller;

import com.pacman.hospital.domain.laboratory.dto.LabOrderDto;
import com.pacman.hospital.domain.laboratory.service.LabOrderService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/lab-orders")
public class LabOrderController {

    private final LabOrderService labOrderService;

    public LabOrderController(LabOrderService labOrderService) {
        this.labOrderService = labOrderService;
    }

    @GetMapping
    public ResponseEntity<List<LabOrderDto>> getAllOrders() {
        return ResponseEntity.ok(labOrderService.getAll());
    }

    @PostMapping
    public ResponseEntity<LabOrderDto> createOrder(
        @RequestBody @Valid LabOrderDto labOrderDto,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        LabOrderDto created = labOrderService.createOrder(labOrderDto);
        URI location = uriComponentsBuilder
            .path("/api/lab-orders/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabOrderDto> updateOrder(
        @PathVariable Long id,
        @RequestBody @Valid LabOrderDto labOrderDto
    ) {
        LabOrderDto updated = labOrderService.updateOrder(id, labOrderDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabOrderDto> getOrderById(@PathVariable Long id) {
        LabOrderDto labOrderDto = labOrderService.getOrderById(id);
        return ResponseEntity.ok(labOrderDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        labOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabOrderDto>> getOrdersForPatient(
        @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
            labOrderService.getOrdersForPatient(patientId)
        );
    }

    @PostMapping(
        value = "/{id}/report",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<LabOrderDto> attachReport(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(labOrderService.attachReport(id, file));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<LabOrderDto> changeStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> body
    ) {
        String status = body != null ? body.get("status") : null; // Accepts JSON body: {"status":"COMPLETED"}
        return ResponseEntity.ok(labOrderService.changeStatus(id, status));
    }
}
//when to use request param, and when to use request body,
//RequestParam is used to extract query parameters, form parameters, or parts of multipart requests.
// It's typically used for simple data types like strings, numbers, or booleans that are
// passed in the URL or as form data. For example, in a GET request to filter results, you might use RequestParam to get the filter criteria.
//RequestBody, on the other hand, is used to bind the entire body of an HTTP request to
// a Java object. It's commonly used in POST, PUT, or PATCH requests where the client
// sends complex data structures, often in JSON or XML format. For example, when creating
// or updating a resource, you would use RequestBody to receive the data representing that resource.
