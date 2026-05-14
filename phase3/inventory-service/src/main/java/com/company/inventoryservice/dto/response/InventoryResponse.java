package com.company.inventoryservice.dto.response;

import com.company.inventoryservice.util.StatusEnum;

public record InventoryResponse(
        String inventoryGuid,
        int availableUnits,
        String productName,
        String description,
        Double pricePerUnit,
        String productGuid,
        StatusEnum status
) {
}
