package com.example.exercise2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequest {
    @NotNull(message = "Order name is required.")
    @NotBlank(message = "Order name must not be blank.")
    private String orderName;
    @NotNull(message = "Item is required.")
    @Valid
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
