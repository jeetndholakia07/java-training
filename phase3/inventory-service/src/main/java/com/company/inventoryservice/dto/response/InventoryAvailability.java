package com.company.inventoryservice.dto.response;

public record InventoryAvailability(
        String productGuid,
        boolean available,
        int requestedQty,
        int availableQty,
        String message
) {
}