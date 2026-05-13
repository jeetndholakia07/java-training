package com.company.inventoryservice.service;

import com.company.inventoryservice.dto.*;
import com.company.inventoryservice.exception.EntityExistsException;
import com.company.inventoryservice.exception.EntityNotFoundException;
import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.repository.InventoryRepository;
import com.company.inventoryservice.repository.ProductFeignClient;
import com.company.inventoryservice.util.StatusEnum;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    GuidService guidService;
    @Mock
    ProductFeignClient productFeignClient;

    @InjectMocks
    private InventoryService inventoryService;

    private CreateInventoryRequest createInventoryRequest;

    @BeforeEach
    void setup(){
        createInventoryRequest = new CreateInventoryRequest();
        createInventoryRequest.setProductGuid("product-guid");
        createInventoryRequest.setAvailableUnits(10);
    }

    @Test
    void createInventory_shouldReturn(){
        ProductResponse productResponse = new ProductResponse();
        when(guidService.verifyUUID("product-guid")).thenReturn(true);
        when(productFeignClient.getProductByGuid("product-guid")).thenReturn(ResponseEntity.ok(productResponse));
        when(inventoryRepository.findByProductGuid("product-guid")).thenReturn(null);
        when(guidService.generateUUID()).thenReturn("inventory-guid");
        inventoryService.createInventory(createInventoryRequest, "admin-user");
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void createInventory_shouldThrowException_whenGuidInvalid() {
        when(guidService.verifyUUID("product-guid"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                inventoryService.createInventory(createInventoryRequest, "user"));
    }

    @Test
    void createInventory_shouldThrowException_whenProductNotFound() {
        when(guidService.verifyUUID("product-guid"))
                .thenReturn(true);

        when(productFeignClient.getProductByGuid("product-guid"))
                .thenThrow(mock(FeignException.NotFound.class));

        assertThrows(EntityNotFoundException.class, () ->
                inventoryService.createInventory(createInventoryRequest, "user"));
    }

    @Test
    void createInventory_shouldThrowException_whenInventoryExists() {
        when(guidService.verifyUUID("product-guid"))
                .thenReturn(true);

        when(productFeignClient.getProductByGuid("product-guid"))
                .thenReturn(ResponseEntity.ok(new ProductResponse()));

        when(inventoryRepository.findByProductGuid("product-guid"))
                .thenReturn(new Inventory());

        assertThrows(EntityExistsException.class, () ->
                inventoryService.createInventory(createInventoryRequest, "user"));
    }

    @Test
    void getInventoryByGuid_shouldReturnInventory() {
        Inventory inventory = new Inventory();
        inventory.setGuid("inv-guid");
        inventory.setProductGuid("product-guid");
        inventory.setAvailableUnits(20);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductName("Laptop");

        when(inventoryRepository.findByProductGuidAndStatusIn(
                eq("product-guid"),
                anyCollection()
        )).thenReturn(inventory);

        when(productFeignClient.getProductByGuid("product-guid"))
                .thenReturn(ResponseEntity.ok(productResponse));

        InventoryResponse response =
                inventoryService.getInventoryByGuid("product-guid");

        assertEquals("Laptop", response.getProductName());
        assertEquals(20, response.getAvailableUnits());
    }

    @Test
    void getInventoryByGuid_shouldThrowException_whenInventoryMissing() {
        when(inventoryRepository.findByProductGuidAndStatusIn(
                eq("product-guid"),
                anyCollection()
        )).thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> inventoryService.getInventoryByGuid("product-guid"));
    }

    @Test
    void addInventoryStock_shouldUpdateInventory() {
        AddInventoryStockRequest request =
                new AddInventoryStockRequest();

        request.setProductGuid("product-guid");
        request.setUnits(5);

        Inventory inventory = new Inventory();
        inventory.setAvailableUnits(10);

        when(guidService.verifyUUID("product-guid"))
                .thenReturn(true);

        when(inventoryRepository.findByProductGuid("product-guid"))
                .thenReturn(inventory);

        inventoryService.addInventoryStock(request, "admin");

        assertEquals(15, inventory.getAvailableUnits());

        verify(inventoryRepository).save(inventory);
    }

    @Test
    void checkoutInventory_shouldReduceStock() {
        Inventory inventory = new Inventory();
        inventory.setProductGuid("product-guid");
        inventory.setAvailableUnits(10);
        inventory.setStatus(StatusEnum.A);

        InventoryCheckItemRequest item =
                new InventoryCheckItemRequest();

        item.setProductGuid("product-guid");
        item.setRequestedQty(2);

        InventoryCheckRequest request =
                new InventoryCheckRequest();

        request.setItems(List.of(item));

        when(inventoryRepository.lockInventories(
                anyList(),
                anyCollection()
        )).thenReturn(List.of(inventory));

        InventoryCheckResponse response =
                inventoryService.checkoutInventory(request);

        assertTrue(response.checkoutAllowed());

        assertEquals(8, inventory.getAvailableUnits());

        verify(inventoryRepository).saveAll(anyList());
    }

    @Test
    void checkoutInventory_shouldFail_whenInsufficientStock() {
        Inventory inventory = new Inventory();
        inventory.setProductGuid("product-guid");
        inventory.setAvailableUnits(1);

        InventoryCheckItemRequest item =
                new InventoryCheckItemRequest();

        item.setProductGuid("product-guid");
        item.setRequestedQty(5);

        InventoryCheckRequest request =
                new InventoryCheckRequest();

        request.setItems(List.of(item));

        when(inventoryRepository.lockInventories(
                anyList(),
                anyCollection()
        )).thenReturn(List.of(inventory));

        InventoryCheckResponse response =
                inventoryService.checkoutInventory(request);

        assertFalse(response.checkoutAllowed());

        verify(inventoryRepository, never()).saveAll(anyList());
    }

    @Test
    void deactivateInventory_shouldDeactivateSuccessfully() {
        Inventory inventory = new Inventory();
        inventory.setStatus(StatusEnum.A);

        when(guidService.verifyUUID("guid"))
                .thenReturn(true);

        when(inventoryRepository.findByGuid("guid"))
                .thenReturn(inventory);

        inventoryService.deactivateInventory("guid", "admin");

        assertEquals(StatusEnum.D, inventory.getStatus());

        verify(inventoryRepository).save(inventory);
    }
}
