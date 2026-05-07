package com.company.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateInventoryRequest {
    @NotNull(message = "Product Guid is required.")
    @NotBlank(message = "Product Guid cannot be blank.")
    private String productGuid;
    @NotNull(message = "Product Guid is required.")
    @Min(value = 1, message = "Available units must be greater than 1.")
    private int availableUnits;

    public CreateInventoryRequest() {
    }

    public String getProductGuid() {
        return productGuid;
    }

    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }
}
