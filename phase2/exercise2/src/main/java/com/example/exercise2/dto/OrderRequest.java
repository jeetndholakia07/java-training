package com.example.exercise2.dto;

import java.util.List;

public class OrderRequest {
    private String orderName;
    private List<OrderItemRequest> items;

    public String getOrderName() {
        return orderName;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
