package com.company.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInventoryRequest(
        @NotNull(message = "Product Guid is required.")
        @NotBlank(message = "Product Guid cannot be blank.")
        String productGuid,
        @NotNull(message = "Product Guid is required.")
        @Min(value = 1, message = "Available units must be greater than 1.")
        int availableUnits
) {
}
