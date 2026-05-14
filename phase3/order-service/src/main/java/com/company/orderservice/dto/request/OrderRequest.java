package com.company.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull(message = "Product guid is required.")
        @NotBlank(message = "Product guid cannot be empty.")
        String productGuid,
        @NotNull(message = "Product quantity is required.")
        @Min(value = 1, message = "Quantity must be atleast 1.")
        int quantity
) {
}
