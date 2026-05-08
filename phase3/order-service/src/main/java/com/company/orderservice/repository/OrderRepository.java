package com.company.orderservice.repository;

import com.company.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderGuid(String guid);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.orderGuid = :orderGuid")
    Order findByOrderGuidWithItems(@Param("orderGuid") String orderGuid);
}
