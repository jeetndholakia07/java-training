package com.company.orderservice.service;

import com.company.orderservice.dto.request.CheckoutRequest;
import com.company.orderservice.dto.request.InventoryCheckItemRequest;
import com.company.orderservice.dto.request.InventoryCheckRequest;
import com.company.orderservice.dto.request.OrderRequest;
import com.company.orderservice.dto.response.*;
import com.company.orderservice.exception.EntityNotFoundException;
import com.company.orderservice.model.Order;
import com.company.orderservice.model.OrderItem;
import com.company.orderservice.repository.InventoryFeignClient;
import com.company.orderservice.repository.OrderRepository;
import com.company.orderservice.repository.ProductFeignClient;
import com.company.orderservice.utils.OrderStatus;
import feign.FeignException;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final GuidService guidService;
    private final ProductFeignClient productFeignClient;
    private final InventoryFeignClient inventoryFeignClient;

    public OrderService(OrderRepository orderRepository, GuidService guidService,
        ProductFeignClient productFeignClient, InventoryFeignClient inventoryFeignClient) {
        this.orderRepository = orderRepository;
        this.guidService = guidService;
        this.productFeignClient = productFeignClient;
        this.inventoryFeignClient = inventoryFeignClient;
    }

    public String addItemsToOrder(List<OrderRequest> requests, String userGuid) {
        Order order = new Order();
        order.setUserGuid(userGuid);
        order.setOrderGuid(guidService.generateUUID());
        order.setCreatedBy(userGuid);
        order.setLastUpdatedBy(userGuid);
        order.setOrderStatus(OrderStatus.P);

        List<String> productGuids = requests.stream()
                .map(OrderRequest::productGuid)
                .toList();
        ProductListResponse productListResponse;
        try {
            productListResponse = productFeignClient.getProductsByGuids(productGuids).getBody();
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException("Product", "Product not found.");
        } catch (FeignException e) {
            throw new RuntimeException("Product service unavailable. Please try again.");
        }

        if (productListResponse == null || productListResponse.products() == null || productListResponse.products().isEmpty()) {
            throw new RuntimeException("No valid products found for provided GUIDs");
        }

        if (productListResponse.missingGuids() != null && !productListResponse.missingGuids().isEmpty()) {
            throw new RuntimeException("Products not found: " + String.join(", ", productListResponse.missingGuids()));
        }

        Map<String, ProductResponse> productMap = productListResponse.products()
                .stream().collect(Collectors.toMap(ProductResponse::productGuid, pr -> pr));

        List<OrderItem> orderItems = new ArrayList<>();
        int totalItems = 0;
        for (OrderRequest request : requests) {
            ProductResponse product = productMap.get(request.productGuid());
            OrderItem item = new OrderItem();
            item.setOrderItemGuid(guidService.generateUUID());
            item.setProductGuid(request.productGuid());
            item.setProductName(product.productName());
            item.setProductDescription(product.description());
            item.setProductPrice(product.pricePerUnit());
            item.setQty(request.quantity());
            Double subTotal = request.quantity() * product.pricePerUnit();
            item.setSubTotal(subTotal);
            item.setCreatedBy(userGuid);
            item.setLastUpdatedBy(userGuid);
            item.setOrder(order);
            totalItems += item.getQty();
            orderItems.add(item);
        }
        order.setTotalItems(totalItems);
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getOrderGuid();
    }

    public void updateOrder(List<OrderRequest> requests, String orderGuid, String userGuid) {
        if (orderGuid == null || !guidService.verifyUUID(orderGuid)) {
            throw new IllegalArgumentException("Invalid order guid.");
        }

        Order order = orderRepository.findByOrderGuid(orderGuid);

        if (order == null) {
            throw new EntityNotFoundException("Order", "Order not found.");
        }

        if (!order.getUserGuid().equals(userGuid)) {
            throw new IllegalStateException("Unauthorized Access");
        }

        if (order.getOrderStatus() == OrderStatus.C) {
            throw new IllegalStateException("Completed orders cannot be updated.");
        }

        order.setLastUpdatedBy(userGuid);

        List<String> productGuids = requests.stream()
                .map(OrderRequest::productGuid)
                .toList();

        ProductListResponse productListResponse;

        try {
            productListResponse = productFeignClient
                    .getProductsByGuids(productGuids)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong. Please try again.");
        }

        if (productListResponse == null || productListResponse.products() == null ||
                productListResponse.products().isEmpty()) {
            throw new RuntimeException("No valid products found.");
        }

        if (productListResponse.missingGuids() != null &&
                !productListResponse.missingGuids().isEmpty()) {
            throw new RuntimeException(
                    "Products not found: " +
                            String.join(", ", productListResponse.missingGuids())
            );
        }

        Map<String, ProductResponse> productMap =
                productListResponse.products()
                        .stream()
                        .collect(Collectors.toMap(
                                ProductResponse::productGuid,
                                p -> p
                        ));

        Map<String, OrderItem> existingItemsMap =
                order.getOrderItems()
                        .stream()
                        .collect(Collectors.toMap(
                                OrderItem::getProductGuid,
                                item -> item
                        ));

        List<OrderItem> updatedItems = new ArrayList<>();

        int totalItems = 0;
        double totalPrice = 0.0;

        for (OrderRequest request : requests) {
            ProductResponse product = productMap.get(request.productGuid());
            OrderItem item;

            if (existingItemsMap.containsKey(request.productGuid())) {
                item = existingItemsMap.get(request.productGuid());
            } else {
                item = new OrderItem();
                item.setOrderItemGuid(guidService.generateUUID());
                item.setCreatedBy(userGuid);
                item.setOrder(order);
            }

            item.setLastUpdatedBy(userGuid);
            item.setProductGuid(product.productGuid());
            item.setProductName(product.productName());
            item.setProductDescription(product.description());
            item.setProductPrice(product.pricePerUnit());
            item.setQty(request.quantity());

            double subTotal = request.quantity() * product.pricePerUnit();
            item.setSubTotal(subTotal);
            totalItems += item.getQty();
            totalPrice += subTotal;
            updatedItems.add(item);
        }
        order.getOrderItems().clear();
        order.getOrderItems().addAll(updatedItems);
        order.setTotalItems(totalItems);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    public PaginatedResponse<OrderResponse> getOrdersByUser(int page, int size, String userGuid) {
        Pageable pageable = PageRequest.of(page, size);
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

    public OrderResponse getOrderByGuid(String orderGuid) {
        if (orderGuid == null || !guidService.verifyUUID(orderGuid)) {
            throw new IllegalArgumentException("Invalid order guid.");
        }
        Order order = orderRepository.findByOrderGuid(orderGuid);
        if (order == null) {
            throw new EntityNotFoundException("Order", "Order not found.");
        }
        return mapToOrderResponse(order);
    }

    public CheckoutResponse checkoutOrder(CheckoutRequest request, String userGuid) {
        final String orderGuid = request.orderGuid();
        if (orderGuid == null || !guidService.verifyUUID(orderGuid)) {
            throw new IllegalArgumentException("Invalid order guid.");
        }
        Order order = orderRepository.findByOrderGuid(orderGuid);
        if (order == null) {
            throw new EntityNotFoundException("Order", "Order not found");
        }
        if (!order.getUserGuid().equals(userGuid)) {
            throw new IllegalStateException("Unauthorized Access");
        }
        InventoryCheckRequest checkRequest = getInventoryCheckRequest(order);
        InventoryCheckResponse response;
        try {
            response = inventoryFeignClient.checkInventoryAvailability(checkRequest).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong. Please try again.");
        }
        if (response == null) {
            throw new RuntimeException("Failed to get inventory checkout.");
        }
        if (!response.checkoutAllowed()) {
            String errorMsg = response.items()
                    .stream()
                    .filter(i -> !i.available())
                    .map(item -> item.productGuid() + " -> only " + item.availableQty() + " available")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Inventory unavailable");
            throw new IllegalArgumentException(errorMsg);
        }
        double totalPrice = 0.0;
        int totalItems = 0;
        for (OrderItem item : order.getOrderItems()) {
            double subTotal = item.getQty() * item.getProductPrice();
            item.setSubTotal(subTotal);
            totalPrice += subTotal;
            totalItems += item.getQty();
        }
        order.setTotalPrice(totalPrice);
        order.setTotalItems(totalItems);
        order.setOrderStatus(OrderStatus.C);
        Order savedOrder = orderRepository.save(order);
        return getCheckoutResponse(savedOrder);
    }

    private static @NonNull CheckoutResponse getCheckoutResponse(Order savedOrder) {
        return new CheckoutResponse(
                savedOrder.getOrderGuid(),
                savedOrder.getTotalPrice(),
                savedOrder.getTotalItems(),
                savedOrder.getOrderStatus(),
                getOrderItemResponses(savedOrder),
                "Order placed successfully."
        );
    }

    private static @NonNull InventoryCheckRequest getInventoryCheckRequest(Order order) {
        if (order == null) {
            throw new EntityNotFoundException("Order", "Order not found.");
        }
        List<InventoryCheckItemRequest> requests = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            InventoryCheckItemRequest request = new InventoryCheckItemRequest(
                    item.getProductGuid(),
                    item.getQty()
            );
            requests.add(request);
        }
        return new InventoryCheckRequest(requests);
    }

    public OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponse = getOrderItemResponses(order);
        return new OrderResponse(
                order.getOrderGuid(),
                order.getTotalItems(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                itemResponse
        );
    }

    private static @NonNull List<OrderItemResponse> getOrderItemResponses(Order order) {
        List<OrderItemResponse> itemResponse = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponse item = new OrderItemResponse(
                    orderItem.getProductGuid(),
                    orderItem.getQty(),
                    orderItem.getProductDescription(),
                    orderItem.getProductPrice(),
                    orderItem.getSubTotal()
            );
            itemResponse.add(item);
        }
        return itemResponse;
    }
}
