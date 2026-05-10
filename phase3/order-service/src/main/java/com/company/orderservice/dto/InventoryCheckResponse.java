package com.company.orderservice.dto;

import java.util.List;

public class InventoryCheckResponse {
    private boolean checkoutAllowed;
    private List<InventoryAvailability> items;

    public boolean isCheckoutAllowed() {
        return checkoutAllowed;
    }

    public void setCheckoutAllowed(boolean checkoutAllowed) {
        this.checkoutAllowed = checkoutAllowed;
    }

    public List<InventoryAvailability> getItems() {
        return items;
    }

    public void setItems(List<InventoryAvailability> items) {
        this.items = items;
    }
}