package com.company.orderservice.dto;

import java.util.List;

public record ProductListResponse(
List<ProductResponse> products,
List<String> missingGuids
){
}