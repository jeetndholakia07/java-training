package com.company.orderservice.dto;
import java.util.List;

public class InventoryCheckRequest{
    List<InventoryCheckItemRequest> items;

    public List<InventoryCheckItemRequest> getItems() {
        return items;
    }

    public void setItems(List<InventoryCheckItemRequest> items) {
        this.items = items;
    }
}