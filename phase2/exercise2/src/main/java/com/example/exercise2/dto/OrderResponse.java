package com.example.exercise2.dto;

import com.example.exercise2.utils.Status;

import java.util.List;

public class OrderResponse {
    private int orderId;
    private String orderName;
    private Double subTotal;
    private Double total;
    private Status status;
    private List<OrderItemResponse> items;

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public Double getTotal() {
        return total;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public Status getStatus() {
        return status;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}
