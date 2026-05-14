package com.company.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryCheckItemRequest(
        @NotNull(message = "Product Guid is required.")
        @NotBlank(message = "Product Guid cannot be empty.")
        String productGuid,
        @NotNull(message = "Qty is required.")
        @Min(value = 1, message = "Qty must be greater than 1.")
        int requestedQty
) {
}