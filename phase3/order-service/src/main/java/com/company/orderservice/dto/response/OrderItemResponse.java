package com.company.orderservice.dto.response;

public record OrderItemResponse(
        String productGuid,
        int qty,
        String description,
        Double pricePerUnit,
        Double subTotal
) {
}
