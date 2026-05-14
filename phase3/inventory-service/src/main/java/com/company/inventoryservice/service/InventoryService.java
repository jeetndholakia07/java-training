package com.company.inventoryservice.service;

import com.company.inventoryservice.dto.request.AddInventoryStockRequest;
import com.company.inventoryservice.dto.request.CreateInventoryRequest;
import com.company.inventoryservice.dto.request.InventoryCheckItemRequest;
import com.company.inventoryservice.dto.request.InventoryCheckRequest;
import com.company.inventoryservice.dto.response.InventoryAvailability;
import com.company.inventoryservice.dto.response.InventoryCheckResponse;
import com.company.inventoryservice.dto.response.InventoryResponse;
import com.company.inventoryservice.dto.response.ProductResponse;
import com.company.inventoryservice.exception.EntityExistsException;
import com.company.inventoryservice.exception.EntityNotFoundException;
import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.repository.InventoryRepository;
import com.company.inventoryservice.repository.ProductFeignClient;
import com.company.inventoryservice.util.StatusEnum;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final GuidService guidService;
    private final ProductFeignClient productFeignClient;

    public InventoryService(InventoryRepository repository, GuidService service, ProductFeignClient productFeignClient) {
        this.inventoryRepository = repository;
        this.guidService = service;
        this.productFeignClient = productFeignClient;
    }

    @Transactional
    public void createInventory(CreateInventoryRequest request, String userGuid) {
        if (!guidService.verifyUUID(request.productGuid())) {
            throw new IllegalArgumentException("Invalid product guid.");
        }
        if (inventoryRepository.findByProductGuid(request.productGuid()) != null) {
            throw new EntityExistsException("Inventory", "Inventory already exists for the product.");
        }
        try {
            ProductResponse response = productFeignClient
                    .getProductByGuid(request.productGuid())
                    .getBody();

            if (response == null) {
                throw new EntityNotFoundException(
                        "Product",
                        "Product doesn't exist."
                );
            }
        }
        catch (FeignException e) {
            throw new RuntimeException("Product service unavailable. Please try again.");
        }
        Inventory inventory = new Inventory();
        inventory.setGuid(guidService.generateUUID());
        inventory.setProductGuid(request.productGuid());
        inventory.setCreatedBy(userGuid);
        inventory.setLastUpdatedBy(userGuid);
        inventory.setStatus(StatusEnum.A);
        inventory.setAvailableUnits(request.availableUnits());
        inventoryRepository.save(inventory);
    }

    public InventoryResponse getInventoryByGuid(String productGuid) {
        Inventory inventory = inventoryRepository.findByProductGuidAndStatusIn(productGuid, Collections.singleton(StatusEnum.A));
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory", "Inventory not found.");
        }
        ProductResponse productResponse;
        try{
            productResponse = productFeignClient.getProductByGuid(productGuid).getBody();
        }
        catch (FeignException e){
            throw new RuntimeException("Product service unavailable. Please try again.");
        }
        if (productResponse == null) {
            throw new EntityNotFoundException("Product", "Product doesn't exist in inventory.");
        }
        return mapInventoryResponse(inventory, productResponse);
    }

    @Transactional
    public void addInventoryStock(AddInventoryStockRequest request, String userGuid) {
        if (!guidService.verifyUUID(request.productGuid())) {
            throw new IllegalArgumentException("Invalid product guid.");
        }
        Inventory inventory = inventoryRepository.findByProductGuid(request.productGuid());
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory", "Inventory not found.");
        }
        inventory.setAvailableUnits(inventory.getAvailableUnits() + request.units());
        inventory.setLastUpdatedBy(userGuid);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public InventoryCheckResponse checkoutInventory(InventoryCheckRequest request) {
        List<String> productGuids = request.items()
                .stream()
                .map(InventoryCheckItemRequest::productGuid)
                .toList();
        List<Inventory> inventories = inventoryRepository.lockInventories(productGuids, Collections.singleton(StatusEnum.A));
        Map<String, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        Inventory::getProductGuid,
                        inv -> inv
                ));
        boolean checkoutAllowed = true;
        List<InventoryAvailability> responses = new ArrayList<>();
        for (InventoryCheckItemRequest item : request.items()) {
            Inventory inventory = inventoryMap.get(item.productGuid());
            if (inventory == null) {
                checkoutAllowed = false;
                responses.add(
                        new InventoryAvailability(
                                item.productGuid(),
                                false,
                                item.requestedQty(),
                                0,
                                "Inventory not found"
                        )
                );
                continue;
            }
            boolean available = inventory.getAvailableUnits() >= item.requestedQty();
            if (!available) {
                checkoutAllowed = false;
            }
            responses.add(new InventoryAvailability(
                    item.productGuid(),
                    available,
                    item.requestedQty(),
                    inventory.getAvailableUnits(),
                    available ? "Available" : "Insufficient stock"
            ));
        }
        if (!checkoutAllowed) {
            return new InventoryCheckResponse(
                    false,
                    responses
            );
        }
        List<Inventory> updatedInventories = new ArrayList<>();
        for (InventoryCheckItemRequest item : request.items()) {
            Inventory inventory = inventoryMap.get(item.productGuid());
            inventory.setAvailableUnits(
                    inventory.getAvailableUnits() - item.requestedQty()
            );
            updatedInventories.add(inventory);
        }
        inventoryRepository.saveAll(updatedInventories);
        return new InventoryCheckResponse(
                checkoutAllowed,
                responses
        );
    }

    public void deactivateInventory(String inventoryGuid, String userGuid) {
        if (!guidService.verifyUUID(inventoryGuid)) {
            throw new IllegalArgumentException("Invalid inventory guid.");
        }
        Inventory inventory = inventoryRepository.findByGuid(inventoryGuid);
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory", "Inventory not found.");
        }
        inventory.setStatus(StatusEnum.D);
        inventory.setLastUpdatedBy(userGuid);
        inventoryRepository.save(inventory);
    }

    public InventoryResponse mapInventoryResponse(Inventory inventory, ProductResponse productResponse) {
        return new InventoryResponse(
                inventory.getGuid(),
                inventory.getAvailableUnits(),
                productResponse.productName(),
                productResponse.description(),
                productResponse.pricePerUnit(),
                productResponse.productGuid(),
                productResponse.status()
        );
    }
}
