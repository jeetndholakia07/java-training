package com.company.productservice.controller;

import com.company.productservice.dto.request.CreateProductRequest;
import com.company.productservice.dto.response.PaginatedResponse;
import com.company.productservice.dto.response.ProductListResponse;
import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.dto.request.UpdateProductRequest;
import com.company.productservice.service.GuidService;
import com.company.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;
    private final GuidService guidService;

    public ProductController(ProductService productService, GuidService guidService) {
        this.productService = productService;
        this.guidService = guidService;
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> createProduct(@RequestBody @Valid CreateProductRequest request,
        @RequestHeader("X-ID") String userGuid) {
        productService.createProduct(request, userGuid);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{guid}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ProductResponse> getProductByGuid(@PathVariable String guid) {
        if (!guidService.verifyUUID(guid)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.getProductByGuid(guid), HttpStatus.OK);
    }

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ProductListResponse> getProductsByGuids(@RequestBody List<String> guids) {
        return new ResponseEntity<>(productService.getProductsByGuids(guids), HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<PaginatedResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search
    ) {
        return new ResponseEntity<>(productService.getPaginatedProducts(page, size, search), HttpStatus.OK);
    }

    @PatchMapping("/{guid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> editProduct(
        @PathVariable String guid,
        @RequestBody @Valid UpdateProductRequest request,
        @RequestHeader("X-ID") String userGuid) {
        if (userGuid == null || !guidService.verifyUUID(userGuid)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!guidService.verifyUUID(guid)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(guid, request, userGuid);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product updated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{guid}")
    public ResponseEntity<Map<String, String>> deactivateProduct(@PathVariable String guid,
        @RequestHeader("X-ID") String userGuid) {
        if (!guidService.verifyUUID(guid)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.deactivateProduct(guid, userGuid);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deactivated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
