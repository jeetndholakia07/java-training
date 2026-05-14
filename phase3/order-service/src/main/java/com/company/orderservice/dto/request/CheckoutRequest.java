package com.company.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
        @NotNull(message = "Order guid is required.")
        @NotBlank(message = "Order guid cannot be empty.")
        String orderGuid
) {
}
