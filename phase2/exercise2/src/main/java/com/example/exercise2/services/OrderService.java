package com.example.exercise2.services;

import com.example.exercise2.dto.OrderItemRequest;
import com.example.exercise2.dto.OrderItemResponse;
import com.example.exercise2.dto.OrderRequest;
import com.example.exercise2.dto.OrderResponse;
import com.example.exercise2.entity.Inventory;
import com.example.exercise2.entity.Order;
import com.example.exercise2.entity.OrderItem;
import com.example.exercise2.exception.EntityNotFoundException;
import com.example.exercise2.exception.OrderStockException;
import com.example.exercise2.repository.InventoryRepository;
import com.example.exercise2.repository.OrderItemRepository;
import com.example.exercise2.repository.OrderRepository;
import com.example.exercise2.utils.Status;
import org.aspectj.weaver.ast.Or;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, InventoryRepository inventoryRepository){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void createOrder(OrderRequest request){
        Order order = new Order();
        List<Integer> inventoryIds = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        order.setOrderName(request.getOrderName());
        order.setStatus(Status.A);
        for(OrderItemRequest item : request.getItems()){
            inventoryIds.add(item.getInventoryId());
        }
        Map<Integer, Inventory> inventoryMap = inventoryRepository.findAllById(inventoryIds)
                .stream()
                .collect(Collectors.toMap(Inventory::getId,i->i));
        if(inventoryMap.size()!=inventoryIds.size()){
            throw new EntityNotFoundException("Inventory","Inventory items not found.");
        }
        for(OrderItemRequest item : request.getItems()){
            Inventory inventoryItem = inventoryMap.get(item.getInventoryId());
            validateInventory(inventoryItem, item);
            OrderItem orderItem = mapOrderItem(order,inventoryItem,item);
            inventoryItem.setAvailableUnits(inventoryItem.getAvailableUnits()-item.getQty());
            orderItem.setInventory(inventoryItem);
            totalPrice += orderItem.getSubTotal();
            orderItems.add(orderItem);
        }
        order.setSubTotal(totalPrice);
        order.setTotal(totalPrice);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }

    public void createOrderWithRetry(OrderRequest request){
        int attempts = 0;
        int maxAttempts = 3;
        while(attempts<maxAttempts){
            try{
                createOrder(request);
                return;
            }
            catch (OptimisticLockingFailureException e){
                attempts++;
                if(attempts==maxAttempts){
                    throw new RuntimeException("Failed to create order due to concurrent updates. Please try again.");
                }
            }
        }
    }

    @Transactional
    public void softDeleteOrder(int orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new EntityNotFoundException("Order","Order not found."));
        if(order.getStatus()==Status.D){
            throw new IllegalStateException("Order already cancelled");
        }
        List<OrderItem> orderItems = orderItemRepository.getOrderItemsByOrder(order);
        for(OrderItem orderItem : orderItems){
            Inventory inventory = orderItem.getInventory();
            inventory.setAvailableUnits(inventory.getAvailableUnits()+orderItem.getQty());
            orderItem.setStatus(Status.D);
        }
        order.setStatus(Status.D);
    }

    public OrderResponse getOrderById(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Order","Order not found."));
        List<OrderItem> orderItems = orderItemRepository.getOrderItemsByOrder(order);
        if(orderItems==null){
            throw new EntityNotFoundException("OrderItem","Order items not found associated with given order");
        }
        return mapOrderResponse(order, orderItems);
    }

    public OrderItem mapOrderItem(Order order, Inventory inventory, OrderItemRequest item){
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setInventory(inventory);
        orderItem.setQty(item.getQty());
        orderItem.setStatus(Status.A);

        double total = inventory.getPrice() * item.getQty();
        orderItem.setSubTotal(total);
        orderItem.setSumPrice(total);
        orderItem.setOrder(order);
        return orderItem;
    }

    public void validateInventory(Inventory inventory, OrderItemRequest request){
        if(inventory.getStatus()!=Status.A){
            throw new IllegalStateException("Inventory item is inactive or deleted.");
        }
        if (inventory.getAvailableUnits() < request.getQty()) {
            throw new OrderStockException("Insufficient stock for item: " + inventory.getDescription()
            );
        }
    }

    public OrderResponse mapOrderResponse(Order order, List<OrderItem> orderItems){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderName(order.getOrderName());
        orderResponse.setOrderId(order.getId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setSubTotal(order.getSubTotal());
        orderResponse.setTotal(order.getTotal());
        List<OrderItemResponse> itemResponseList = new ArrayList<>();
        for(OrderItem orderItem : orderItems){
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setItemName(orderItem.getInventory().getDescription());
            itemResponse.setId(orderItem.getId());
            itemResponse.setQty(orderItem.getQty());
            itemResponse.setStatus(orderItem.getStatus());
            itemResponse.setSumPrice(orderItem.getSumPrice());
            itemResponse.setSubTotal(orderItem.getSubTotal());
            itemResponseList.add(itemResponse);
        }
        orderResponse.setItems(itemResponseList);
        return orderResponse;
    }
}
