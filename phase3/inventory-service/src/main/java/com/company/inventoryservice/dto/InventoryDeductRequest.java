package com.company.inventoryservice.dto;

import java.util.Map;

public record InventoryDeductRequest(Map<String,Integer> items) {
}
