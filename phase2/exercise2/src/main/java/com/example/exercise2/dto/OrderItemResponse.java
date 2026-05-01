package com.example.exercise2.dto;

import com.example.exercise2.utils.Status;

public class OrderItemResponse {
    private int id;
    private String itemName;
    private int qty;
    private Double sumPrice;
    private Double subTotal;
    private Status status;

    public int getQty() {
        return qty;
    }

    public Status getStatus() {
        return status;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public String getItemName() {
        return itemName;
    }

    public int getId() {
        return id;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
