package com.example.exercise2.dto;

public class InventoryRequest {
    private String description;
    private Double price;
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
