package com.company.orderservice.service;

import com.company.orderservice.dto.CheckoutResponse;
import com.company.orderservice.dto.OrderRequest;
import com.company.orderservice.dto.ProductListResponse;
import com.company.orderservice.dto.ProductResponse;
import com.company.orderservice.exception.EntityNotFoundException;
import com.company.orderservice.model.Order;
import com.company.orderservice.model.OrderItem;
import com.company.orderservice.repository.OrderItemRepository;
import com.company.orderservice.repository.OrderRepository;
import com.company.orderservice.repository.ProductFeignClient;
import com.company.orderservice.utils.StatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
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
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, GuidService guidService, ProductFeignClient productFeignClient){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.guidService = guidService;
        this.productFeignClient = productFeignClient;
    }
    public void addItemsToOrder(List<OrderRequest> requests, String userGuid){
        Order order = new Order();
        order.setUserGuid(userGuid);
        order.setOrderGuid(guidService.generateUUID());
        order.setCreatedBy(userGuid);
        order.setLastUpdatedBy(userGuid);
        order.setOrderStatus(StatusEnum.P);

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

        if(productListResponse==null || productListResponse.products()==null || productListResponse.products().isEmpty()){
            throw new RuntimeException("No valid products found for provided GUIDs");
        }

        if(productListResponse.missingGuids()!=null && !productListResponse.missingGuids().isEmpty()){
            throw new RuntimeException("Products not found: " + String.join(", ", productListResponse.missingGuids()));
        }

        Map<String,ProductResponse> productMap = productListResponse.products()
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
            totalItems+=item.getQty();
            orderItems.add(item);
        }
        order.setTotalItems(totalItems);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
    }
}
