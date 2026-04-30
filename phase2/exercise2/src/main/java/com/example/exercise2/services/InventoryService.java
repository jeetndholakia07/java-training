package com.example.exercise2.services;

import com.example.exercise2.dto.InventoryRequest;
import com.example.exercise2.dto.InventoryResponse;
import com.example.exercise2.entity.Inventory;
import com.example.exercise2.repository.InventoryRepository;
import com.example.exercise2.utils.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    public InventoryService(InventoryRepository repository){
        this.inventoryRepository = repository;
    }

    public void createInventory(InventoryRequest request){
        Inventory inventory = new Inventory();
        inventory.setDescription(request.getDescription());
        inventory.setPrice(request.getPrice());
        inventory.setAvailableUnits(request.getAvailableUnits());
        inventory.setStatus(Status.A);
        inventoryRepository.save(inventory);
    }

    public void updateInventory(int id,InventoryRequest request){
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Inventory not found."));
        inventory.setDescription(request.getDescription());
        inventory.setPrice(request.getPrice());
        inventory.setAvailableUnits(request.getAvailableUnits());
        inventoryRepository.save(inventory);
    }

    public List<InventoryResponse> getAllInventory(){
        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public InventoryResponse getInventoryById(int id){
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Inventory not found"));
        return mapToResponse(inventory);
    }

    public void deleteInventory(int id){
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Inventory not found."));
        inventory.setStatus(Status.D);
        inventoryRepository.save(inventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setDescription(inventory.getDescription());
        response.setPrice(inventory.getPrice());
        response.setAvailableUnits(inventory.getAvailableUnits());
        response.setStatus(inventory.getStatus());
        return response;
    }
}
