package com.company.orderservice.repository;

import com.company.orderservice.dto.InventoryCheckRequest;
import com.company.orderservice.dto.InventoryCheckResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "inventory-service")
public interface InventoryFeignClient {
    @PostMapping("/v1/inventory/checkout")
    public ResponseEntity<InventoryCheckResponse> checkInventoryAvailability(@RequestBody @Valid InventoryCheckRequest request);
}
