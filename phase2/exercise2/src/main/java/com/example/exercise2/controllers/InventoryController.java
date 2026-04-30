package com.example.exercise2.controllers;

import com.example.exercise2.dto.InventoryRequest;
import com.example.exercise2.dto.InventoryResponse;
import com.example.exercise2.services.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(InventoryService service){
        this.inventoryService = service;
    }
    @PostMapping("/")
    public ResponseEntity<String> addInventory(@RequestBody InventoryRequest request){
        inventoryService.createInventory(request);
        return ResponseEntity.ok().body("Inventory created successfully");
    }
    @GetMapping("/")
    public ResponseEntity<List<InventoryResponse>> getAllInventoryItems(){
        return ResponseEntity.ok().body(inventoryService.getAllInventory());
    }
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable int id){
        return ResponseEntity.ok().body(inventoryService.getInventoryById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> editInventory(@PathVariable int id, @RequestBody InventoryRequest request){
        inventoryService.updateInventory(id,request);
        return ResponseEntity.ok().body("Inventory updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteInventory(@PathVariable int id){
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok().body("Deleted inventory successfully");
    }
}