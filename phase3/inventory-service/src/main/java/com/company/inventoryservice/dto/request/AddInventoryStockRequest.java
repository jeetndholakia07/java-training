package com.company.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddInventoryStockRequest(
        @NotNull(message = "Product Guid is required.")
        @NotBlank(message = "Product Guid cannot be empty.")
        String productGuid,
        @NotNull(message = "Units is required.")
        @Min(value = 1, message = "Units must be greater than 1.")
        int units
) {
}
