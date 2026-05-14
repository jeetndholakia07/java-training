package com.company.inventoryservice.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryCheckRequest(
        @NotNull(message = "Inventory items are required.")
        List<InventoryCheckItemRequest> items
) {
}