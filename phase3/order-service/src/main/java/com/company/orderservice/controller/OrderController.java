package com.company.orderservice.controller;

import com.company.orderservice.dto.CheckoutResponse;
import com.company.orderservice.dto.OrderRequest;
import com.company.orderservice.dto.OrderResponse;
import com.company.orderservice.dto.PaginatedResponse;
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
    private final GuidService guidService;
    public OrderController(OrderService orderService, GuidService guidService){
        this.orderService = orderService;
        this.guidService = guidService;
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/cart")
    public ResponseEntity<Map<String,String>> addOrderToCart(@RequestBody @Valid List<OrderRequest> request,
        @RequestHeader("X-ID") String userGuid){
        if(!guidService.verifyUUID(userGuid)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        orderService.addItemsToOrder(request,userGuid);
        Map<String,String> response = new HashMap<>();
        response.put("message","Order created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/")
    public ResponseEntity<PaginatedResponse<OrderResponse>> getOrdersByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestHeader("X-ID") String userGuid
    ){
        if(!guidService.verifyUUID(userGuid)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(orderService.getOrdersByUser(page,size,userGuid), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{guid}")
    public ResponseEntity<OrderResponse> getOrderByGuid(@PathVariable String orderGuid){
        return new ResponseEntity<>(orderService.getOrderByGuid(orderGuid), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkoutOrder(@RequestBody String orderGuid){
        return new ResponseEntity<>(orderService.checkoutOrder(orderGuid), HttpStatus.OK);
    }
}
