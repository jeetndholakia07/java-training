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
import com.company.inventoryservice.repository.InventoryRepositoryTest;
import com.company.inventoryservice.repository.ProductFeignClient;
import com.company.inventoryservice.util.StatusEnum;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private GuidService guidService;
    @Mock
    private ProductFeignClient productFeignClient;
    @InjectMocks
    private InventoryService inventoryService;
    private String userGuid;
    private Inventory inventory;

    @BeforeEach
    void setup(){
        userGuid = "user-guid";
        inventory = new Inventory();
        inventory.setProductGuid("guid-1");
        inventory.setAvailableUnits(5);
    }

    private Inventory createInventory(String productGuid, int units) {
        Inventory inventory = new Inventory();
        inventory.setProductGuid(productGuid);
        inventory.setAvailableUnits(units);
        inventory.setStatus(StatusEnum.A);
        return inventory;
    }

    private InventoryCheckItemRequest createItem(String guid, int qty) {
        return new InventoryCheckItemRequest(guid, qty);
    }

    @Test
    void createInventory_InvalidGuid(){
        CreateInventoryRequest request = new CreateInventoryRequest("xyz",5);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(false);
        assertThatThrownBy(()->inventoryService.createInventory(request, userGuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid product guid.");
    }

    @Test
    void createInventory_Exists(){
        CreateInventoryRequest request = new CreateInventoryRequest("guid-1",1);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(true);
        when(inventoryRepository.findByProductGuid(request.productGuid())).thenReturn(inventory);
        assertThatThrownBy(()->inventoryService.createInventory(request, userGuid))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Inventory already exists for the product.");
    }

    @Test
    void createInventory_productNotFound(){
        CreateInventoryRequest request = new CreateInventoryRequest("guid-2",1);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(true);
        when(inventoryRepository.findByProductGuid(request.productGuid())).thenReturn(null);
        ResponseEntity<ProductResponse> response = ResponseEntity.ok(null);
        when(productFeignClient.getProductByGuid(request.productGuid())).thenReturn(response);
        assertThatThrownBy(()->inventoryService.createInventory(request, userGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product doesn't exist.");
    }

    @Test
    void createInventory_feignClientFails(){
        CreateInventoryRequest request = new CreateInventoryRequest("guid-2",1);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(true);
        when(inventoryRepository.findByProductGuid(request.productGuid())).thenReturn(null);
        Request feignRequest = Request.create(
                Request.HttpMethod.GET,
                "/v1/products/guid-2",
                Collections.emptyMap(),
                null,
                StandardCharsets.UTF_8,
                null
        );
        Response response = Response.builder()
                .status(500)
                .reason("Server error")
                .request(feignRequest)
                .build();
        FeignException feignException = FeignException.errorStatus(
                        "getProductByGuid",
                        response
                );
        when(productFeignClient.getProductByGuid(request.productGuid())).thenThrow(feignException);
        assertThatThrownBy(() ->
                inventoryService.createInventory(request, userGuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product service unavailable. Please try again.");
    }

    @Test
    void createInventory_success(){
        CreateInventoryRequest request = new CreateInventoryRequest("guid-2",1);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(true);
        when(inventoryRepository.findByProductGuid(request.productGuid())).thenReturn(null);
        ProductResponse productResponse = new ProductResponse(
                "laptop",
                "coding",
                100.0,
                request.productGuid(),
                StatusEnum.A
        );
        when(productFeignClient.getProductByGuid(request.productGuid())).thenReturn(new ResponseEntity<>(productResponse, HttpStatusCode.valueOf(HttpStatus.SC_OK)));
        inventoryService.createInventory(request, userGuid);
        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository).save(captor.capture());
        assertThat(captor.getValue().getProductGuid()).isEqualTo(request.productGuid());
        assertThat(captor.getValue().getAvailableUnits()).isEqualTo(request.availableUnits());
    }

    @Test
    void getInventoryByGuid_notFound(){
        String productGuid = "xyz";
        when(inventoryRepository.findByProductGuidAndStatusIn(productGuid, Collections.singleton(StatusEnum.A))).thenReturn(null);
        assertThatThrownBy(()->inventoryService.getInventoryByGuid(productGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Inventory not found.");
    }

    @Test
    void getInventoryByGuid_feignClientFails(){
        String productGuid = "guid-2";
        when(inventoryRepository.findByProductGuidAndStatusIn(productGuid, Collections.singleton(StatusEnum.A))).thenReturn(inventory);
        Request feignRequest = Request.create(
                Request.HttpMethod.GET,
                "/v1/products/guid-2",
                Collections.emptyMap(),
                null,
                StandardCharsets.UTF_8,
                null
        );
        Response response = Response.builder()
                .status(500)
                .reason("Server error")
                .request(feignRequest)
                .build();
        FeignException feignException = FeignException.errorStatus(
                "getProductByGuid",
                response
        );
        when(productFeignClient.getProductByGuid(productGuid)).thenThrow(feignException);
        assertThatThrownBy(()->inventoryService.getInventoryByGuid(productGuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product service unavailable. Please try again.");
    }

    @Test
    void getInventoryByGuid_productNotFound(){
        String productGuid = "guid-2";
        when(inventoryRepository.findByProductGuidAndStatusIn(productGuid, Collections.singleton(StatusEnum.A))).thenReturn(inventory);
        when(productFeignClient.getProductByGuid(productGuid)).thenReturn(ResponseEntity.ok(null));
        assertThatThrownBy(()->inventoryService.getInventoryByGuid(productGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product doesn't exist in inventory.");
    }

    @Test
    void getInventoryByGuid_success(){
        String productGuid = "guid-2";
        when(inventoryRepository.findByProductGuidAndStatusIn(productGuid, Collections.singleton(StatusEnum.A))).thenReturn(inventory);
        ProductResponse productResponse = new ProductResponse("mobile", "gaming", 2000.0, "guid-2", StatusEnum.A);
        when(productFeignClient.getProductByGuid(productGuid)).thenReturn(ResponseEntity.ok(productResponse));
        InventoryResponse response = inventoryService.getInventoryByGuid(productGuid);
        assertThat(response.productGuid()).isEqualTo(productGuid);
    }

    @Test
    void addInventoryStock_InvalidGuid(){
        AddInventoryStockRequest request = new AddInventoryStockRequest("xyz",5);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(false);
        assertThatThrownBy(()->inventoryService.addInventoryStock(request,userGuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid product guid.");
    }

    @Test
    void addInventoryStock_NotFound(){
        AddInventoryStockRequest request = new AddInventoryStockRequest("guid-2",5);
        when(guidService.verifyUUID(request.productGuid())).thenReturn(true);
        when(inventoryRepository.findByProductGuid(request.productGuid())).thenReturn(null);
        assertThatThrownBy(()->inventoryService.addInventoryStock(request, userGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Inventory not found.");
    }

    @Test
    void addInventoryStock_success(){
            AddInventoryStockRequest request = new AddInventoryStockRequest("guid-1",5);
            when(guidService.verifyUUID(request.productGuid())).thenReturn(true);
            when(inventoryRepository.findByProductGuid(request.productGuid())).thenReturn(inventory);
            int expectedUnits = inventory.getAvailableUnits() + request.units();
            inventoryService.addInventoryStock(request, userGuid);
            ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
            verify(inventoryRepository).save(captor.capture());
            assertThat(captor.getValue().getProductGuid()).isEqualTo(request.productGuid());
            assertThat(captor.getValue().getAvailableUnits()).isEqualTo(expectedUnits);
    }

    @Test
    void deleteInventory_invalidGuid(){
        String guid = "xyz";
        when(guidService.verifyUUID(guid)).thenReturn(false);
        assertThatThrownBy(()->inventoryService.deactivateInventory(guid, userGuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid inventory guid.");
    }

    @Test
    void deleteInventory_notFound(){
        String guid = "guid-x";
        when(guidService.verifyUUID(guid)).thenReturn(true);
        when(inventoryRepository.findByGuid(guid)).thenReturn(null);
        assertThatThrownBy(()->inventoryService.deactivateInventory(guid, userGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Inventory not found.");
    }

    @Test
    void deleteInventory_success(){
        String guid = "guid-1";
        when(guidService.verifyUUID(guid)).thenReturn(true);
        when(inventoryRepository.findByGuid(guid)).thenReturn(inventory);
        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        inventoryService.deactivateInventory(guid, userGuid);
        verify(inventoryRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(StatusEnum.D);
        assertThat(captor.getValue().getLastUpdatedBy()).isEqualTo(userGuid);
    }

    @Test
    void checkout_success(){
        Inventory inventory1 = createInventory("guid-1", 10);
        Inventory inventory2 = createInventory("guid-2", 5);
        InventoryCheckRequest request = new InventoryCheckRequest(
                List.of(createItem("guid-1", 2), createItem("guid-2", 3))
        );
        when(inventoryRepository.lockInventories(anyList(),anySet())).thenReturn(
                List.of(inventory1, inventory2)
        );
        InventoryCheckResponse response = inventoryService.checkoutInventory(request);
        assertThat(response.checkoutAllowed()).isEqualTo(true);
        assertThat(inventory1.getAvailableUnits()).isEqualTo(8);
        assertThat(inventory2.getAvailableUnits()).isEqualTo(2);
        verify(inventoryRepository).saveAll(anyList());
    }

    @Test
    void checkout_inventoryNotFound(){
        InventoryCheckRequest request = new InventoryCheckRequest(
               List.of(createItem("guid-1", 2))
        );
        when(inventoryRepository.lockInventories(anyList(), anySet())).thenReturn(Collections.emptyList());
        InventoryCheckResponse response = inventoryService.checkoutInventory(request);
        assertThat(response.checkoutAllowed()).isEqualTo(false);
        assertThat(response.items()).hasSize(1);
        InventoryAvailability availability =
                response.items().get(0);
        assertThat(availability.available()).isFalse();
        assertThat(availability.message())
                .isEqualTo("Inventory not found");
        verify(inventoryRepository, never())
                .saveAll(anyList());
    }

    @Test
    void checkout_insufficientStock(){
        Inventory inventory =
                createInventory("guid-1", 2);

        InventoryCheckRequest request =
                new InventoryCheckRequest(
                        List.of(createItem("guid-1", 5))
                );
        when(inventoryRepository.lockInventories(
                anyList(),
                anySet()
        )).thenReturn(List.of(inventory));

        InventoryCheckResponse response =
                inventoryService.checkoutInventory(request);

        assertThat(response.checkoutAllowed()).isFalse();

        InventoryAvailability availability = response.items().get(0);

        assertThat(availability.available()).isFalse();
        assertThat(availability.message()).isEqualTo("Insufficient stock");

        assertThat(inventory.getAvailableUnits()).isEqualTo(2);

        verify(inventoryRepository, never()).saveAll(anyList());
    }

    @Test
    void checkout_failure(){
        Inventory inventory1 = createInventory("guid-1", 10);
        Inventory inventory2 = createInventory("guid-2", 1);

        InventoryCheckRequest request =
                new InventoryCheckRequest(
                        List.of(createItem("guid-1", 2), createItem("guid-2", 5))
                );
        when(inventoryRepository.lockInventories(
                anyList(),
                anySet()
        )).thenReturn(List.of(inventory1, inventory2));

        InventoryCheckResponse response = inventoryService.checkoutInventory(request);
        assertThat(response.checkoutAllowed()).isFalse();
        assertThat(inventory1.getAvailableUnits()).isEqualTo(10);
        assertThat(inventory2.getAvailableUnits()).isEqualTo(1);
        verify(inventoryRepository, never()).saveAll(anyList());
    }
}
