package com.company.inventoryservice.repository;

import com.company.inventoryservice.model.Inventory;
import com.company.inventoryservice.util.StatusEnum;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByProductGuid(String productGuid);

    Inventory findByProductGuidAndStatusIn(String productGuid, Collection<StatusEnum> statuses);

    Inventory findByGuid(String inventoryGuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.productGuid IN :productGuids AND i.status IN :statuses")
    List<Inventory> lockInventories(List<String> productGuids, Collection<StatusEnum> statuses);
}
