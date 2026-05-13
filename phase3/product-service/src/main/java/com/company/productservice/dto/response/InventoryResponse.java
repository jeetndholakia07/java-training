package com.company.productservice.dto.response;

import com.company.productservice.util.StatusEnum;

public class InventoryResponse {
    private String inventoryGuid;
    private int availableUnits;
    private String productName;
    private String description;
    private Double pricePerUnit;
    private String productGuid;
    private StatusEnum status;

    public InventoryResponse() {
    }

    public String getInventoryGuid() {
        return inventoryGuid;
    }

    public void setInventoryGuid(String inventoryGuid) {
        this.inventoryGuid = inventoryGuid;
    }

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
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

    public String getProductGuid() {
        return productGuid;
    }

    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}

