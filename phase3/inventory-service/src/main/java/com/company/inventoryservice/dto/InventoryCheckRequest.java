package com.company.inventoryservice.dto;

import java.util.Map;

public record InventoryCheckRequest(Map<String, Integer> items) {
}
