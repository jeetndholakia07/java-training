package com.company.orderservice.repository;

import com.company.orderservice.dto.request.InventoryCheckRequest;
import com.company.orderservice.dto.response.InventoryCheckResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "inventory-service")
public interface InventoryFeignClient {
    @PostMapping("/v1/inventory/checkout")
    ResponseEntity<InventoryCheckResponse> checkInventoryAvailability(@RequestBody @Valid InventoryCheckRequest request);
}
