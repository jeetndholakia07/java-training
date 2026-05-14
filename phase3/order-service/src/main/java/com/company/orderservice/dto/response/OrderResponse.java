package com.company.orderservice.dto.response;

import com.company.orderservice.utils.OrderStatus;

import java.util.List;

public record OrderResponse(
        String orderGuid,
        int totalItems,
        Double totalPrice,
        OrderStatus orderStatus,
        List<OrderItemResponse> orderItems
) {
}
