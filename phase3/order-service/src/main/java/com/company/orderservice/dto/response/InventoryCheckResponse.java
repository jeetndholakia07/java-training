package com.company.orderservice.dto.response;

import java.util.List;

public record InventoryCheckResponse(
        boolean checkoutAllowed,
        List<InventoryAvailability> items
) {
}