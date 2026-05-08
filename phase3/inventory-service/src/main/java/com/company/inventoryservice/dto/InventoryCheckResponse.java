package com.company.inventoryservice.dto;

import java.util.Map;
import java.util.Set;

public record InventoryCheckResponse(Map<String, Boolean> availability, Set<String> missingProducts) {
}
