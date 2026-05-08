package com.company.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull(message = "Product guid is required.")
    @NotBlank(message = "Product guid cannot be empty.")
    private String productGuid;
    @NotNull(message = "Product quantity is required.")
    @Min(value = 1, message = "Quantity must be atleast 1.")
    private int quantity;

    public OrderRequest() {
    }

    public String getProductGuid() {
        return productGuid;
    }

    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
