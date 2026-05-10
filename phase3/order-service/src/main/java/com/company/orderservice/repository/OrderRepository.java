package com.company.orderservice.repository;

import com.company.orderservice.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @EntityGraph(attributePaths = {"orderItems"})
    Page<Order> findByUserGuid(String userGuid, Pageable pageable);
    @EntityGraph(attributePaths = {"orderItems"})
    Order findByOrderGuid(String orderGuid);
}