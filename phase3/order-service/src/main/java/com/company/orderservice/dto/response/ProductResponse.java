package com.company.orderservice.dto.response;

import com.company.orderservice.utils.ProductStatus;

public record ProductResponse(
        String productName,
        String description,
        Double pricePerUnit,
        String productGuid,
        ProductStatus status
) {
}

