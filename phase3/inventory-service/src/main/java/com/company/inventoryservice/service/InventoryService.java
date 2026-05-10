package com.company.inventoryservice.service;

import com.company.inventoryservice.dto.*;
import com.company.inventoryservice.exception.EntityExistsException;
import com.company.inventoryservice.exception.EntityNotFoundException;
import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.repository.InventoryRepository;
import com.company.inventoryservice.repository.ProductFeignClient;
import com.company.inventoryservice.util.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final GuidService guidService;
    private final ProductFeignClient productFeignClient;
    public InventoryService(InventoryRepository repository, GuidService service, ProductFeignClient productFeignClient){
        this.inventoryRepository = repository;
        this.guidService = service;
        this.productFeignClient = productFeignClient;
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
    public InventoryResponse getInventoryByGuid(String productGuid){
        Inventory inventory = inventoryRepository.findInventoryByProductGuidAndStatusIn(productGuid, Collections.singleton(StatusEnum.A));
        if(inventory==null){
            throw new EntityNotFoundException("Inventory","Inventory not found");
        }
        ProductResponse productResponse = productFeignClient.getProductByGuid(productGuid).getBody();
        if(productResponse==null){
            throw new EntityNotFoundException("Product","Product doesn't exist in inventory.");
        }
        return mapInventoryResponse(inventory,productResponse);
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

    @Transactional
    public InventoryCheckResponse checkoutInventory(InventoryCheckRequest request){
        List<String> productGuids = request.getItems()
                .stream()
                .map(InventoryCheckItemRequest::getProductGuid)
                .toList();
        List<Inventory> inventories = inventoryRepository.lockInventories(productGuids, Collections.singleton(StatusEnum.A));
        Map<String, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        Inventory::getProductGuid,
                        inv->inv
                ));
        boolean checkoutAllowed = true;
        List<InventoryAvailability> responses = new ArrayList<>();
        for(InventoryCheckItemRequest item : request.getItems()){
            Inventory inventory = inventoryMap.get(item.getProductGuid());
            if(inventory==null){
                checkoutAllowed = false;
                responses.add(
                        new InventoryAvailability(
                                item.getProductGuid(),
                                false,
                                item.getRequestedQty(),
                                0,
                                "Inventory not found"
                        )
                );
                continue;
            }
            boolean available = inventory.getAvailableUnits()>=item.getRequestedQty();
            if(!available){
                checkoutAllowed = false;
            }
            responses.add(new InventoryAvailability(
                    item.getProductGuid(),
                    available,
                    item.getRequestedQty(),
                    inventory.getAvailableUnits(),
                    available ? "Available" : "Insufficient stock"
            ));
        }
        if(!checkoutAllowed){
            return new InventoryCheckResponse(
                    false,
                    responses
            );
        }
        for(InventoryCheckItemRequest item: request.getItems()){
            Inventory inventory = inventoryMap.get(item.getProductGuid());
            inventory.setAvailableUnits(inventory.getAvailableUnits()- item.getRequestedQty());
        }
        inventoryRepository.saveAll(inventories);
        return new InventoryCheckResponse(
                checkoutAllowed,
                responses
        );
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
    public InventoryResponse mapInventoryResponse(Inventory inventory, ProductResponse productResponse){
        InventoryResponse response = new InventoryResponse();
        response.setInventoryGuid(inventory.getGuid());
        response.setDescription(productResponse.getDescription());
        response.setProductGuid(inventory.getProductGuid());
        response.setAvailableUnits(inventory.getAvailableUnits());
        response.setPricePerUnit(productResponse.getPricePerUnit());
        response.setStatus(productResponse.getStatus());
        return response;
    }
}
