package com.company.orderservice.controller;

import com.company.orderservice.dto.*;
import com.company.orderservice.service.GuidService;
import com.company.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/cart")
    public ResponseEntity<Map<String,String>> addOrderToCart(@RequestBody @Valid List<OrderRequest> request,
        @RequestHeader("X-ID") String userGuid){
        String orderGuid = orderService.addItemsToOrder(request,userGuid);
        Map<String,String> response = new HashMap<>();
        response.put("message","Order created successfully");
        response.put("orderGuid", orderGuid);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/")
    public ResponseEntity<PaginatedResponse<OrderResponse>> getOrdersByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestHeader("X-ID") String userGuid
    ){
        return new ResponseEntity<>(orderService.getOrdersByUser(page,size,userGuid), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{guid}")
    public ResponseEntity<OrderResponse> getOrderByGuid(@PathVariable String guid){
        return new ResponseEntity<>(orderService.getOrderByGuid(guid), HttpStatus.OK);
    }
    @PutMapping("/{guid}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, String>> updateCart(@PathVariable String guid,
    @RequestBody @Valid List<OrderRequest> requests, @RequestHeader("X-ID") String userGuid){
        orderService.updateOrder(requests, guid, userGuid);
        Map<String,String> response = new HashMap<>();
        response.put("message","Order updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkoutOrder(@RequestBody @Valid CheckoutRequest request,
        @RequestHeader("X-ID") String userGuid){
        return new ResponseEntity<>(orderService.checkoutOrder(request, userGuid), HttpStatus.OK);
    }
}
