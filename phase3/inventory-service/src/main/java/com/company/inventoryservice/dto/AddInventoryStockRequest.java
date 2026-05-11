package com.company.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddInventoryStockRequest {
    @NotNull(message = "Product Guid is required.")
    @NotBlank(message = "Product Guid cannot be empty.")
    private String productGuid;
    @NotNull(message = "Units is required.")
    @Min(value = 1, message = "Units must be greater than 1.")
    int units;

    public String getProductGuid() {
        return productGuid;
    }

    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
