package com.company.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutRequest {
    @NotNull(message = "Order guid is required.")
    @NotBlank(message = "Order guid cannot be empty.")
    private String orderGuid;

    public String getOrderGuid() {
        return orderGuid;
    }

    public void setOrderGuid(String orderGuid) {
        this.orderGuid = orderGuid;
    }
}
