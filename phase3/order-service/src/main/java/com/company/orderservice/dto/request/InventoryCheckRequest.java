package com.company.orderservice.dto.request;

import java.util.List;

public record InventoryCheckRequest(
        List<InventoryCheckItemRequest> items
) {
}