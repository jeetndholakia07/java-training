package com.company.inventoryservice.repository;

import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.util.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findInventoryByProductGuid(String productGuid);
    Inventory findInventoryByProductGuidAndStatusIn(String productGuid, Collection<StatusEnum> statuses);
    List<Inventory> findByProductGuidAndStatusIn(Collection<String> productGuids, Collection<StatusEnum> statuses);
}
