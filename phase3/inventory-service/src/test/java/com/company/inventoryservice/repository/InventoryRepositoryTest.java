package com.company.inventoryservice.repository;

import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.util.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class InventoryRepositoryTest {
    @Autowired
    private InventoryRepository inventoryRepository;
    private final String userGuid = "user-guid";

    public Inventory createInventory(String guid, String productGuid, int units, StatusEnum status) {
        Inventory inventory = new Inventory();
        inventory.setGuid(guid);
        inventory.setProductGuid(productGuid);
        inventory.setAvailableUnits(units);
        inventory.setStatus(status);
        inventory.setCreatedBy(userGuid);
        return inventoryRepository.save(inventory);
    }

    @Test
    void shouldFindByProductGuid() {
        createInventory("guid-1", "product-1", 5, StatusEnum.A);
        Inventory found = inventoryRepository.findByProductGuid("product-1");
        assertThat(found).isNotNull();
        assertThat(found.getProductGuid()).isEqualTo("product-1");
    }

    @Test
    void shouldFindByProductGuidAndStatusIn() {
        createInventory("guid-1", "product-1", 5, StatusEnum.A);
        createInventory("guid-2", "product-2", 10, StatusEnum.I);
        Inventory result = inventoryRepository.findByProductGuidAndStatusIn("product-1", Collections.singleton(StatusEnum.A));
        assertThat(result).isNotNull();
        assertThat(result.getProductGuid()).isEqualTo("product-1");
        assertThat(result.getStatus()).isEqualTo(StatusEnum.A);
    }

    @Test
    void shouldFindByGuid() {
        Inventory inventory = createInventory("guid-3", "product-3", 20, StatusEnum.A);
        Inventory result = inventoryRepository.findByGuid(inventory.getGuid());
        assertThat(result).isNotNull();
        assertThat(result.getGuid()).isEqualTo(inventory.getGuid());
        assertThat(result.getProductGuid()).isEqualTo(inventory.getProductGuid());
    }

    @Test
    void shouldLockInventory(){
        createInventory("guid-1", "product-1", 5, StatusEnum.A);
        createInventory("guid-2", "product-2", 10, StatusEnum.I);
        createInventory("guid-3", "product-3", 20, StatusEnum.A);
        List<Inventory> inventories = inventoryRepository.lockInventories(List.of("product-1", "product-3"), Collections.singleton(StatusEnum.A));
        assertThat(inventories).isNotNull();
        assertThat(inventories).hasSize(2);
        assertThat(inventories.get(0).getProductGuid()).isEqualTo("product-1");
        assertThat(inventories.get(1).getProductGuid()).isEqualTo("product-3");
    }
}
