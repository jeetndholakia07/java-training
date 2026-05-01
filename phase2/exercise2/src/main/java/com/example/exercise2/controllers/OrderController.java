package com.example.exercise2.controllers;

import com.example.exercise2.dto.OrderRequest;
import com.example.exercise2.dto.OrderResponse;
import com.example.exercise2.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService service){
        this.orderService = service;
    }
    @PostMapping("")
    public ResponseEntity<Map<String,String>> addOrder(@RequestBody @Valid OrderRequest orderRequest){
        Map<String,String> response = new HashMap<>();
        orderService.createOrderWithRetry(orderRequest);
        response.put("message","Order created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable int id){
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> cancelOrder(@PathVariable int id){
        Map<String,String> response = new HashMap<>();
        orderService.softDeleteOrder(id);
        response.put("message","Order cancelled successfully");
        return ResponseEntity.ok().body(response);
    }
}
