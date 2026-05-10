package com.company.inventoryservice.dto;

public class InventoryAvailability {
    private String productGuid;
    private boolean available;
    private int requestedQty;
    private int availableQty;
    private String message;

    public InventoryAvailability() {
    }

    public InventoryAvailability(
            String productGuid,
            boolean available,
            int requestedQty,
            int availableQty,
            String message
    ) {
        this.productGuid = productGuid;
        this.available = available;
        this.requestedQty = requestedQty;
        this.availableQty = availableQty;
        this.message = message;
    }

    public String getProductGuid() {
        return productGuid;
    }

    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(int requestedQty) {
        this.requestedQty = requestedQty;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}