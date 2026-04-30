package com.example.exercise2.services;

import com.example.exercise2.dto.OrderRequest;
import com.example.exercise2.repository.InventoryRepository;
import com.example.exercise2.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository, InventoryRepository inventoryRepository){
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void createOrder(OrderRequest request){

    }
}
