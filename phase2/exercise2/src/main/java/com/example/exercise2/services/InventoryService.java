package com.example.exercise2.services;

import com.example.exercise2.dto.InventoryRequest;
import com.example.exercise2.dto.InventoryResponse;
import com.example.exercise2.dto.PaginatedResponse;
import com.example.exercise2.entity.Inventory;
import com.example.exercise2.exception.EntityExistsException;
import com.example.exercise2.exception.EntityNotFoundException;
import com.example.exercise2.repository.InventoryRepository;
import com.example.exercise2.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    public InventoryService(InventoryRepository repository){
        this.inventoryRepository = repository;
    }

    public void createInventory(InventoryRequest request){
        Optional<Inventory> existingInv = inventoryRepository.findInventoryByDescription(request.getDescription());
        if(existingInv.isPresent()){
            throw new EntityExistsException("Inventory","Inventory already exists.");
        }
        Inventory inventory = new Inventory();
        inventory.setDescription(request.getDescription());
        inventory.setPrice(request.getPrice());
        inventory.setAvailableUnits(request.getAvailableUnits());
        inventory.setStatus(Status.A);
        inventoryRepository.save(inventory);
    }

    public void updateInventory(int id,InventoryRequest request){
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Inventory","Inventory not found."));
        inventory.setDescription(request.getDescription());
        inventory.setPrice(request.getPrice());
        inventory.setAvailableUnits(request.getAvailableUnits());
        inventoryRepository.save(inventory);
    }

    public PaginatedResponse<InventoryResponse> getAllInventory(int page, int size, String search){
        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventoryPage;
        if(search!=null && !search.isEmpty()){
            inventoryPage = inventoryRepository.findByStatusAndDescriptionContainingIgnoreCase(Status.A,search,pageable);
        } else {
            inventoryPage = inventoryRepository.findByStatus(Status.A,pageable);
        }
        List<InventoryResponse> data = inventoryPage
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();
        return new PaginatedResponse<>(
                data,
                inventoryPage.getNumber(),
                inventoryPage.getSize(),
                inventoryPage.getTotalPages()
        );
    }

    public InventoryResponse getInventoryById(int id){
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Inventory","Inventory not found."));
        return mapToResponse(inventory);
    }

    public void deleteInventory(int id){
        Inventory inventory = inventoryRepository.findInventoryByIdAndStatusIn(id, Collections.singleton(Status.A))
                .orElseThrow(()->new EntityNotFoundException("Inventory","Inventory not found."));
        inventory.setStatus(Status.D);
        inventoryRepository.save(inventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setDescription(inventory.getDescription());
        response.setPrice(inventory.getPrice());
        response.setAvailableUnits(inventory.getAvailableUnits());
        response.setStatus(inventory.getStatus());
        return response;
    }
}
