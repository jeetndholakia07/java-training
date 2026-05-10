package com.company.inventoryservice.dto;

import java.util.List;

public record InventoryCheckResponse(
        boolean checkoutAllowed,
        List<InventoryAvailability> items
) {
}