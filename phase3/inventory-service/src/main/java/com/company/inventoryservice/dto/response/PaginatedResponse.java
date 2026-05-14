package com.company.inventoryservice.dto.response;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> data,
        int page,
        int size,
        int totalPages
) {
}

