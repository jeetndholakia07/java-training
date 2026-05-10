package com.company.inventoryservice.controller;

import com.company.inventoryservice.dto.*;
import com.company.inventoryservice.service.GuidService;
import com.company.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    private final GuidService guidService;
    public InventoryController(InventoryService inventoryService, GuidService guidService){
        this.inventoryService = inventoryService;
        this.guidService = guidService;
    }
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,String>> createInventory(@RequestBody CreateInventoryRequest request,
        @RequestHeader("X-ID") String userGuid){
        if(userGuid==null || !guidService.verifyUUID(userGuid)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Map<String,String> response = new HashMap<>();
        response.put("message","Inventory created successfully.");
        inventoryService.createInventory(request, userGuid);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/{guid}")
    public ResponseEntity<Map<String,String>> addInventoryStock(@PathVariable String guid,
            @RequestBody int units, @RequestHeader("X-ID") String userGuid){
        if(userGuid==null || !guidService.verifyUUID(userGuid)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Map<String,String> response = new HashMap<>();
        inventoryService.addInventoryStock(guid, units, userGuid);
        response.put("message","Inventory updated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{guid}")
    public ResponseEntity<InventoryResponse> getInventoryByGuid(@PathVariable String productGuid){
        if(productGuid==null || !guidService.verifyUUID(productGuid)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(inventoryService.getInventoryByGuid(productGuid), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/checkout")
    public ResponseEntity<InventoryCheckResponse> getInventoryAvailability(
            @RequestBody @Valid InventoryCheckRequest request
    ){
        return new ResponseEntity<>(inventoryService.checkoutInventory(request), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{guid}")
    public ResponseEntity<Map<String,String>> deactivateInventory(@PathVariable String guid,
       @RequestHeader("X-ID") String userGuid) {
        if (userGuid == null || !guidService.verifyUUID(userGuid)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Map<String, String> response = new HashMap<>();
        inventoryService.deactivateInventory(guid, userGuid);
        response.put("message", "Inventory deactivated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}