package com.company.productservice.dto;

import jakarta.validation.constraints.Min;

public class UpdateProductRequest {
    private String description;
    @Min(value = 5, message = "Price per unit must be greater than 5.")
    private Double pricePerUnit;

    public UpdateProductRequest() {
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
