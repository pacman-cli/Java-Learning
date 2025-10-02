package com.puspo.scalablekafkaapp.inventoryservice.application;

import com.puspo.scalablekafkaapp.inventoryservice.domain.Inventory;

import java.util.List;

public interface InventoryService {
    List<Inventory> getAllItems();

    Inventory addItem(Inventory inventory);

    void handleOrderCreated(String message);
}
