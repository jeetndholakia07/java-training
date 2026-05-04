package com.example.exercise2.controllers;

import com.example.exercise2.dto.InventoryOptionalRequest;
import com.example.exercise2.dto.InventoryRequest;
import com.example.exercise2.dto.InventoryResponse;
import com.example.exercise2.dto.PaginatedResponse;
import com.example.exercise2.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(InventoryService service){
        this.inventoryService = service;
    }
    @PostMapping("")
    public ResponseEntity<Map<String, String>> addInventory(@RequestBody @Valid InventoryRequest request){
        Map<String,String> response = new HashMap<>();
        inventoryService.createInventory(request);
        response.put("message","Inventory created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("")
    public ResponseEntity<PaginatedResponse<InventoryResponse>> getAllInventoryItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search
    ){
        return ResponseEntity.ok().body(inventoryService.getAllInventory(page,size,search));
    }
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable int id){
        return ResponseEntity.ok().body(inventoryService.getInventoryById(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> editInventory(@PathVariable int id, @RequestBody @Valid InventoryOptionalRequest request){
        Map<String,String> response = new HashMap<>();
        inventoryService.updateInventory(id,request);
        response.put("message","Inventory updated successfully");
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> softDeleteInventory(@PathVariable int id){
        Map<String,String> response = new HashMap<>();
        inventoryService.deleteInventory(id);
        response.put("message","Inventory deleted successfully");
        return ResponseEntity.ok().body(response);
    }
}