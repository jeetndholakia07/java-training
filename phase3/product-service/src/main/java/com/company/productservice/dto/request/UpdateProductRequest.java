package com.company.productservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record UpdateProductRequest(
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Description cannot be empty."
        )
        String description,
        @Min(value = 5, message = "Price per unit must be greater than 5.")
        Double pricePerUnit
) {
}
