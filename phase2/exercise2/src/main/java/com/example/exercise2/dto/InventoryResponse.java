package com.example.exercise2.dto;

import com.example.exercise2.utils.Status;

public class InventoryResponse {
    private int id;
    private String description;
    private Double price;
    private Integer availableUnits;
    private Status status;

    public String getDescription() {
        return description;
    }

    public Integer getAvailableUnits() {
        return availableUnits;
    }

    public Double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
