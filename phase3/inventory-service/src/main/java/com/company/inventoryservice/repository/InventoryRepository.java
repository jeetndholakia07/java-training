package com.company.inventoryservice.repository;

import com.company.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findInventoryByProductGuid(String productGuid);
    Inventory getInventoryByGuid(String guid);
}
