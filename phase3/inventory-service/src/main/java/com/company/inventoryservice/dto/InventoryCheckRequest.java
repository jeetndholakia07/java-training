package com.company.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class InventoryCheckRequest {
    @NotBlank(message = "Inventory items are required.")
    private List<InventoryCheckItemRequest> items;

    public List<InventoryCheckItemRequest> getItems() {
        return items;
    }

    public void setItems(List<InventoryCheckItemRequest> items) {
        this.items = items;
    }
}