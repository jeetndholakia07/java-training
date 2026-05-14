package com.company.productservice.dto.response;

import com.company.productservice.util.StatusEnum;

public record ProductResponse(
        String productName,
        String description,
        Double pricePerUnit,
        String productGuid,
        StatusEnum status
) {
}