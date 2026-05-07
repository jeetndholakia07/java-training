package com.company.inventoryservice.service;

import com.company.inventoryservice.dto.CreateInventoryRequest;
import com.company.inventoryservice.exception.EntityExistsException;
import com.company.inventoryservice.exception.EntityNotFoundException;
import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.repository.InventoryRepository;
import com.company.inventoryservice.util.StatusEnum;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final GuidService guidService;
    public InventoryService(InventoryRepository repository, GuidService service){
        this.inventoryRepository = repository;
        this.guidService = service;
    }
    public void createInventory(CreateInventoryRequest request, String userGuid){
        if(!guidService.verifyUUID(request.getProductGuid())){
            throw new IllegalArgumentException("Invalid product guid");
        }
        if(inventoryRepository.findInventoryByProductGuid(request.getProductGuid())!=null){
            throw new EntityExistsException("Inventory","Inventory not found");
        }
        Inventory inventory = new Inventory();
        inventory.setGuid(guidService.generateUUID());
        inventory.setProductGuid(request.getProductGuid());
        inventory.setCreatedBy(userGuid);
        inventory.setLastUpdatedBy(userGuid);
        inventory.setStatus(StatusEnum.A);
        inventory.setAvailableUnits(request.getAvailableUnits());
        inventoryRepository.save(inventory);
    }
    public void addInventoryStock(String inventoryGuid, int units, String userGuid){
        if(!guidService.verifyUUID(inventoryGuid)){
            throw new IllegalArgumentException("Invalid inventory guid");
        }
        Inventory inventory = inventoryRepository.findInventoryByProductGuid(inventoryGuid);
        if(inventory==null){
            throw new EntityNotFoundException("Inventory","Inventory not found");
        }
        if(units<1){
            throw new IllegalArgumentException("Inventory available units must be greater than 1.");
        }
        inventory.setAvailableUnits(units);
        inventory.setLastUpdatedBy(userGuid);
        inventoryRepository.save(inventory);
    }
    public void updateInventoryInactive(String inventoryGuid, String userGuid){
        if(!guidService.verifyUUID(inventoryGuid)){
            throw new IllegalArgumentException("Invalid inventory guid");
        }
        Inventory inventory = inventoryRepository.findInventoryByProductGuid(inventoryGuid);
        if(inventory==null){
            throw new EntityNotFoundException("Inventory","Inventory not found");
        }
        inventory.setStatus(StatusEnum.I);
        inventory.setLastUpdatedBy(userGuid);
        inventoryRepository.save(inventory);
    }
    public void deactivateInventory(String inventoryGuid, String userGuid){
        if(!guidService.verifyUUID(inventoryGuid)){
            throw new IllegalArgumentException("Invalid inventory guid");
        }
        Inventory inventory = inventoryRepository.findInventoryByProductGuid(inventoryGuid);
        if(inventory==null){
            throw new EntityNotFoundException("Inventory","Inventory not found");
        }
        inventory.setStatus(StatusEnum.D);
        inventory.setLastUpdatedBy(userGuid);
        inventoryRepository.save(inventory);
    }
}
