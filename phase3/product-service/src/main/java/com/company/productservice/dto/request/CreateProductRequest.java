package com.company.productservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
        @NotNull(message = "Product name is required.")
        @NotBlank(message = "Product name cannot be empty.")
        String productName,
        @NotNull(message = "Product description is required.")
        @NotBlank(message = "Product description cannot be empty.")
        String description,
        @NotNull(message = "Price per unit is required.")
        @Min(value = 5, message = "Price per unit must be greater than 5.")
        Double pricePerUnit
) {
}
