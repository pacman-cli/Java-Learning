package com.puspo.scalablekafkaapp.inventoryservice.application.impl;

import com.puspo.scalablekafkaapp.inventoryservice.application.InventoryService;
import com.puspo.scalablekafkaapp.inventoryservice.domain.Inventory;
import com.puspo.scalablekafkaapp.inventoryservice.infrastructure.persistence.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory addItem(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    @KafkaListener(topics = "order.created", groupId = "inventory-service-group")
    public void handleOrderCreated(String message) {
        System.out.println("Received message from order.created(Inventory Service): " + message);
        // Logic: e.g., reduce inventory based on order details
    }

}
