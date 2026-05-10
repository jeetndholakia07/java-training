package com.company.orderservice.service;

import com.company.orderservice.dto.*;
import com.company.orderservice.exception.EntityNotFoundException;
import com.company.orderservice.model.Order;
import com.company.orderservice.model.OrderItem;
import com.company.orderservice.repository.InventoryFeignClient;
import com.company.orderservice.repository.OrderItemRepository;
import com.company.orderservice.repository.OrderRepository;
import com.company.orderservice.repository.ProductFeignClient;
import com.company.orderservice.utils.OrderStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final GuidService guidService;
    private final ProductFeignClient productFeignClient;
    private InventoryFeignClient inventoryFeignClient;
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, GuidService guidService,
                        ProductFeignClient productFeignClient, InventoryFeignClient inventoryFeignClient){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.guidService = guidService;
        this.productFeignClient = productFeignClient;
        this.inventoryFeignClient = inventoryFeignClient;
    }
    public void addItemsToOrder(List<OrderRequest> requests, String userGuid){
        Order order = new Order();
        order.setUserGuid(userGuid);
        order.setOrderGuid(guidService.generateUUID());
        order.setCreatedBy(userGuid);
        order.setLastUpdatedBy(userGuid);
        order.setOrderStatus(OrderStatus.P);

        List<String> productGuids = requests.stream()
                .map(OrderRequest::getProductGuid)
                .toList();
        ProductListResponse productListResponse;
        try{
            productListResponse = productFeignClient.getProductsByGuids(productGuids).getBody();
        }
        catch (HttpClientErrorException e){
            throw new RuntimeException("Error calling Product Service:"+e.getMessage());
        }

        if(productListResponse==null || productListResponse.getProducts()==null || productListResponse.getProducts().isEmpty()){
            throw new RuntimeException("No valid products found for provided GUIDs");
        }

        if(productListResponse.getMissingGuids()!=null && !productListResponse.getMissingGuids().isEmpty()){
            throw new RuntimeException("Products not found: " + String.join(", ", productListResponse.getMissingGuids()));
        }

        Map<String,ProductResponse> productMap = productListResponse.getProducts()
                .stream().collect(Collectors.toMap(ProductResponse::getProductGuid, pr->pr));

        List<OrderItem> orderItems = new ArrayList<>();
        int totalItems = 0;
        for(OrderRequest request : requests){
            ProductResponse product = productMap.get(request.getProductGuid());
            OrderItem item = new OrderItem();
            item.setOrderItemGuid(guidService.generateUUID());
            item.setProductGuid(request.getProductGuid());
            item.setProductName(product.getProductName());
            item.setProductDescription(product.getDescription());
            item.setProductPrice(product.getPricePerUnit());
            item.setQty(request.getQuantity());
            item.setSubTotal(0.0);
            item.setCreatedBy(userGuid);
            item.setLastUpdatedBy(userGuid);
            item.setOrder(order);
            totalItems+=item.getQty();
            orderItems.add(item);
        }
        order.setTotalItems(totalItems);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }
    public PaginatedResponse<OrderResponse> getOrdersByUser(int page, int size, String userGuid){
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> ordersPage = orderRepository.findByUserGuid(userGuid, pageable);
        List<OrderResponse> orderResponse = ordersPage.getContent()
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
        return new PaginatedResponse<>(
                orderResponse,
                ordersPage.getNumber(),
                ordersPage.getSize(),
                ordersPage.getTotalPages()
        );
    }
    public OrderResponse getOrderByGuid(String orderGuid){
        if(orderGuid==null || !guidService.verifyUUID(orderGuid)){
            throw new IllegalArgumentException("Invalid order guid.");
        }
        Order order = orderRepository.findByOrderGuid(orderGuid);
        if(order==null){
            throw new EntityNotFoundException("Order","Order not found.");
        }
        return mapToOrderResponse(order);
    }

    public CheckoutResponse checkoutOrder(String orderGuid){
        if(orderGuid==null || !guidService.verifyUUID(orderGuid)){
            throw new IllegalArgumentException("Invalid order guid.");
        }
        Order order = orderRepository.findByOrderGuid(orderGuid);
        InventoryCheckRequest checkRequest = getInventoryCheckRequest(order);
        InventoryCheckResponse response = null;
        try{
            response = inventoryFeignClient.checkInventoryAvailability(checkRequest).getBody();
        }
        catch (Exception e){
        }
        if(response==null){
            throw new RuntimeException("Failed to get inventory checkout.");
        }
        if(!response.isCheckoutAllowed()){
            String errorMsg = response.getItems()
                    .stream()
                    .filter(i->!i.isAvailable())
                    .map(item->item.getProductGuid()+" -> only " + item.getAvailableQty() + " available")
                    .reduce((a,b)->a+", "+b)
                    .orElse("Inventory unavailable");
            throw new IllegalArgumentException(errorMsg);
        }
        double totalPrice = 0.0;
        int totalItems = 0;
        for(OrderItem item : order.getOrderItems()){
            double subTotal = item.getQty() * item.getProductPrice();
            item.setSubTotal(subTotal);
            totalPrice+=subTotal;
            totalItems+=item.getQty();
        }
        order.setTotalPrice(totalPrice);
        order.setTotalItems(totalItems);
        order.setOrderStatus(OrderStatus.C);
        Order savedOrder = orderRepository.save(order);
        CheckoutResponse checkoutResponse = new CheckoutResponse();

        checkoutResponse.setOrderGuid(savedOrder.getOrderGuid());
        checkoutResponse.setTotalItems(savedOrder.getTotalItems());
        checkoutResponse.setTotalPrice(savedOrder.getTotalPrice());
        checkoutResponse.setStatus(savedOrder.getOrderStatus());
        checkoutResponse.setItems(getOrderItemResponses(savedOrder));
        checkoutResponse.setMessage("Order placed successfully.");
        return checkoutResponse;
    }

    private static @NonNull InventoryCheckRequest getInventoryCheckRequest(Order order) {
        if(order ==null){
            throw new EntityNotFoundException("Order","Order not found.");
        }
        List<InventoryCheckItemRequest> requests = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()){
            InventoryCheckItemRequest request = new InventoryCheckItemRequest();
            request.setProductGuid(item.getProductGuid());
            request.setRequestedQty(item.getQty());
            requests.add(request);
        }
        InventoryCheckRequest checkRequest = new InventoryCheckRequest();
        checkRequest.setItems(requests);
        return checkRequest;
    }

    public OrderResponse mapToOrderResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setOrderGuid(order.getOrderGuid());
        response.setOrderStatus(order.getOrderStatus());
        response.setTotalItems(order.getTotalItems());
        response.setTotalPrice(order.getTotalPrice());
        List<OrderItemResponse> itemResponse = getOrderItemResponses(order);
        response.setOrderItems(itemResponse);
        return response;
    }

    private static @NonNull List<OrderItemResponse> getOrderItemResponses(Order order) {
        List<OrderItemResponse> itemResponse = new ArrayList<>();
        for(OrderItem orderItem : order.getOrderItems()){
            OrderItemResponse item = new OrderItemResponse();
            item.setDescription(orderItem.getProductDescription());
            item.setQty(orderItem.getQty());
            item.setPricePerUnit(orderItem.getProductPrice());
            item.setSubTotal(orderItem.getSubTotal());
            item.setProductGuid(orderItem.getProductGuid());
            itemResponse.add(item);
        }
        return itemResponse;
    }
}
