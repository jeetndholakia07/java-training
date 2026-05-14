package com.company.inventoryservice.dto.response;

import java.util.List;

public record InventoryCheckResponse(
        boolean checkoutAllowed,
        List<InventoryAvailability> items
) {
}