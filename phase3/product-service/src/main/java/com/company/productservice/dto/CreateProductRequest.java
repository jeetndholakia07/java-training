package com.company.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateProductRequest {
    @NotNull(message = "Product name is required.")
    @NotBlank(message = "Product name cannot be empty.")
    private String productName;
    @NotNull(message = "Product description is required.")
    @NotBlank(message = "Product description cannot be empty.")
    private String description;
    @NotNull(message = "Price per unit is required.")
    @Min(value = 5, message = "Price per unit must be greater than 5.")
    private Double pricePerUnit;

    public CreateProductRequest() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
