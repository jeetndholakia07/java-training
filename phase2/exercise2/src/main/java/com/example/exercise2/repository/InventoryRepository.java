package com.example.exercise2.repository;

import com.example.exercise2.entity.Inventory;
import com.example.exercise2.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    Optional<Inventory> findInventoryByDescription(String description);
    List<Inventory> findInventoriesByStatus(Status status);
    Optional<Inventory> findInventoryByIdAndStatusIn(int id, Collection<Status> statuses);
    Page<Inventory> findByStatusAndDescriptionContainingIgnoreCase(Status status, String description, Pageable pageable);
    Page<Inventory> findByStatus(Status status, Pageable pageable);
}
