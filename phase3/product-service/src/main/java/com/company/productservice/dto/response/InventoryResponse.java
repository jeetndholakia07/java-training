package com.company.productservice.dto.response;

import com.company.productservice.util.StatusEnum;

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

