package com.company.orderservice.repository;

import com.company.orderservice.dto.response.ProductListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "product-service")
public interface ProductFeignClient {
    @PostMapping("/v1/products/list")
    ResponseEntity<ProductListResponse> getProductsByGuids(@RequestBody List<String> guids);
}
