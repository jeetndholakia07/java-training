package com.company.orderservice.dto;

import com.company.orderservice.utils.StatusEnum;

import java.util.List;

public class CheckoutResponse {
    private String orderGuid;
    private int totalItems;
    private StatusEnum orderStatus;
    private Double totalPrice;
    private List<OrderItemResponse> orderItems;

    public CheckoutResponse() {
    }

    public String getOrderGuid() {
        return orderGuid;
    }

    public void setOrderGuid(String orderGuid) {
        this.orderGuid = orderGuid;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public StatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(StatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
}
