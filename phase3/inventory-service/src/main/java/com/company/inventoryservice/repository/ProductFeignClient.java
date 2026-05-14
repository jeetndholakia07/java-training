package com.company.inventoryservice.repository;

import com.company.inventoryservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product-service")
public interface ProductFeignClient {
    @GetMapping("/v1/products/{guid}")
    ResponseEntity<ProductResponse> getProductByGuid(@PathVariable String guid);
}
