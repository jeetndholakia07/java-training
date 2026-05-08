package com.company.orderservice.repository;

import com.company.orderservice.dto.InventoryCheckRequest;
import com.company.orderservice.dto.InventoryCheckResponse;
import com.company.orderservice.dto.InventoryDeductRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "inventory-service")
public interface InventoryFeignClient {
    @PostMapping("/v1/inventory/check")
    public ResponseEntity<InventoryCheckResponse> checkInventoryAvailability(@RequestBody @Valid InventoryCheckRequest request);
    @PostMapping("/v1/inventory/update-stock")
    public ResponseEntity<Map<String,String>> updateInventoryStock(@RequestBody @Valid InventoryDeductRequest request);
}
