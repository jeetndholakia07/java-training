package com.company.orderservice.dto;

import java.util.Map;

public record InventoryCheckRequest(Map<String, Integer> items) {
}
