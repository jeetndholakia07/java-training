package com.company.orderservice.dto.response;

import com.company.orderservice.utils.OrderStatus;

import java.util.List;

public record CheckoutResponse(
        String orderGuid,
        Double totalPrice,
        int totalItems,
        OrderStatus status,
        List<OrderItemResponse> items,
        String message
) {
}
