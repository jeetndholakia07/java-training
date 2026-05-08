package com.company.orderservice.dto;

import java.util.Map;

public record InventoryDeductRequest(Map<String,Integer> items) {
}
