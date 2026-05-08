package com.company.orderservice.controller;

import com.company.orderservice.dto.OrderRequest;
import com.company.orderservice.service.GuidService;
import com.company.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
