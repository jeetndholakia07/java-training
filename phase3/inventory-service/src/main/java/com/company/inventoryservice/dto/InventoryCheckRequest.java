package com.company.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InventoryCheckRequest {
    @NotNull(message = "Inventory items are required.")
    private List<InventoryCheckItemRequest> items;

    public List<InventoryCheckItemRequest> getItems() {
        return items;
    }

    public void setItems(List<InventoryCheckItemRequest> items) {
        this.items = items;
    }
}