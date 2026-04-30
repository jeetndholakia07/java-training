package com.example.exercise2.dto;

public class OrderItemRequest {
    private String inventoryId;
    private int qty;

    public int getQty() {
        return qty;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
