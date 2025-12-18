package com.puspo.scalablekafkaapp.inventoryservice.api;

import com.puspo.scalablekafkaapp.inventoryservice.application.InventoryService;
import com.puspo.scalablekafkaapp.inventoryservice.domain.Inventory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllItems() {
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @PostMapping
    public ResponseEntity<Inventory> addItem(@RequestBody Inventory item) {
        return ResponseEntity.ok(inventoryService.addItem(item));
    }
}
