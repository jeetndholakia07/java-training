package com.example.exercise2.dto;

import com.example.exercise2.utils.Status;

public class OrderResponse {
    private String orderName;
    private Double subTotal;
    private Double total;
    private Status status;

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

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
