package com.company.productservice.dto.response;

import java.util.List;

public record ProductListResponse(
        List<ProductResponse> products,
        List<String> missingGuids
) {
}