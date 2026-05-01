package com.example.exercise2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventoryRequest {
    @NotNull(message = "Description is required.")
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    @NotNull(message = "Price is required.")
    @Min(value = 1, message = "Price must be atleast 1.")
    private Double price;
    @NotNull(message = "Available units is required.")
    @Min(value = 1, message = "Available units must be atleast 1.")
    private Integer availableUnits;

    public Double getPrice() {
        return price;
    }

    public Integer getAvailableUnits() {
        return availableUnits;
    }

    public String getDescription() {
        return description;
    }

    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
