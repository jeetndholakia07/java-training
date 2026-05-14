package com.company.orderservice.service;

import com.company.orderservice.dto.request.CheckoutRequest;
import com.company.orderservice.dto.request.OrderRequest;
import com.company.orderservice.dto.response.*;
import com.company.orderservice.exception.EntityNotFoundException;
import com.company.orderservice.model.Order;
import com.company.orderservice.model.OrderItem;
import com.company.orderservice.repository.InventoryFeignClient;
import com.company.orderservice.repository.OrderRepository;
import com.company.orderservice.repository.ProductFeignClient;
import com.company.orderservice.utils.OrderStatus;
import com.company.orderservice.utils.ProductStatus;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private GuidService guidService;
    @Mock
    private ProductFeignClient productFeignClient;
    @Mock
    private InventoryFeignClient inventoryFeignClient;
    @InjectMocks
    private OrderService orderService;
    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setOrderGuid("order-guid");
        order.setUserGuid("user-guid");
        order.setOrderItems(new ArrayList<>());
        order.setOrderStatus(OrderStatus.P);
    }

    @Test
    void addItemsToOrder_success() {
        List<OrderRequest> requests = List.of(
                new OrderRequest("product-1", 2)
        );

        ProductResponse product = new ProductResponse(
                "Laptop",
                "Gaming",
                1000.0,
                "product-1",
                ProductStatus.A
        );

        ProductListResponse productList = new ProductListResponse(List.of(product), List.of());
        when(guidService.generateUUID()).thenReturn("order-guid", "item-guid");
        when(productFeignClient.getProductsByGuids(anyList())).thenReturn(ResponseEntity.ok(productList));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        String result = orderService.addItemsToOrder(requests, "user-guid");

        assertThat(result).isEqualTo("order-guid");

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void addItemsToOrder_productServiceUnavailable() {

        List<OrderRequest> requests =
                List.of(new OrderRequest("product-1", 2));

        when(productFeignClient.getProductsByGuids(anyList())).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() ->
                orderService.addItemsToOrder(
                        requests,
                        "user-guid"
                ))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(
                        "Product service unavailable. Please try again."
                );
    }

    @Test
    void addItemsToOrder_missingProducts() {
        ProductResponse validProduct = new ProductResponse(
                "Laptop",
                "Gaming",
                1000.0,
                "product-1",
                ProductStatus.A
        );

        ProductListResponse response = new ProductListResponse(
                List.of(validProduct),
                List.of("missing-1")
        );

        when(productFeignClient.getProductsByGuids(anyList())).thenReturn(ResponseEntity.ok(response));

        assertThatThrownBy(() ->
                orderService.addItemsToOrder(
                        List.of(
                                new OrderRequest("missing-1", 1)
                        ),
                        "user-guid"
                ))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Products not found");
    }

    @Test
    void updateOrder_invalidGuid() {
        when(guidService.verifyUUID("bad-guid")).thenReturn(false);

        assertThatThrownBy(() ->
                orderService.updateOrder(
                        List.of(),
                        "bad-guid",
                        "user-guid"
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid order guid.");
    }

    @Test
    void updateOrder_orderNotFound() {
        when(guidService.verifyUUID("order-guid")).thenReturn(true);
        when(orderRepository.findByOrderGuid("order-guid")).thenReturn(null);

        assertThatThrownBy(() ->
                orderService.updateOrder(
                        List.of(),
                        "order-guid",
                        "user-guid"
                ))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Order not found.");
    }

    @Test
    void updateOrder_unauthorized() {
        order.setUserGuid("another-user");
        when(guidService.verifyUUID("order-guid")).thenReturn(true);
        when(orderRepository.findByOrderGuid("order-guid")).thenReturn(order);

        assertThatThrownBy(() ->
                orderService.updateOrder(
                        List.of(),
                        "order-guid",
                        "user-guid"
                ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Unauthorized Access");
    }

    @Test
    void updateOrder_completedOrder() {
        order.setOrderStatus(OrderStatus.C);
        when(guidService.verifyUUID("order-guid")).thenReturn(true);
        when(orderRepository.findByOrderGuid("order-guid")).thenReturn(order);

        assertThatThrownBy(() ->
                orderService.updateOrder(
                        List.of(),
                        "order-guid",
                        "user-guid"
                ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Completed orders cannot be updated."
                );
    }

    @Test
    void getOrderByGuid_success() {
        when(guidService.verifyUUID("order-guid")).thenReturn(true);
        when(orderRepository.findByOrderGuid("order-guid")).thenReturn(order);
        OrderResponse response = orderService.getOrderByGuid("order-guid");
        assertThat(response.orderGuid()).isEqualTo("order-guid");
    }

    @Test
    void getOrderByGuid_invalidGuid() {

        when(guidService.verifyUUID("bad-guid")).thenReturn(false);

        assertThatThrownBy(() -> orderService.getOrderByGuid("bad-guid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void checkoutOrder_success() {
        OrderItem item = new OrderItem();
        item.setProductGuid("product-1");
        item.setQty(2);
        item.setProductPrice(100.0);
        order.setOrderItems(List.of(item));

        CheckoutRequest request = new CheckoutRequest("order-guid");

        InventoryAvailability availability = new InventoryAvailability(
                "product-1",
                true,
                2,
                10,
                "Available"
        );

        InventoryCheckResponse inventoryResponse = new InventoryCheckResponse(
                true,
                List.of(availability)
        );

        when(guidService.verifyUUID("order-guid")).thenReturn(true);
        when(orderRepository.findByOrderGuid("order-guid")).thenReturn(order);
        when(inventoryFeignClient.checkInventoryAvailability(any())).thenReturn(ResponseEntity.ok(inventoryResponse));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        CheckoutResponse response =
                orderService.checkoutOrder(
                        request,
                        "user-guid"
                );

        assertThat(response.totalPrice()).isEqualTo(200.0);
        assertThat(response.totalItems()).isEqualTo(2);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.C);
    }

    @Test
    void checkoutOrder_insufficientInventory() {
        OrderItem item = new OrderItem();
        item.setProductGuid("product-1");
        item.setQty(5);
        order.setOrderItems(List.of(item));

        InventoryAvailability availability = new InventoryAvailability(
                "product-1",
                false,
                5,
                2,
                "Insufficient stock"
        );

        InventoryCheckResponse inventoryResponse =
                new InventoryCheckResponse(
                        false,
                        List.of(availability)
                );

        when(guidService.verifyUUID("order-guid")).thenReturn(true);
        when(orderRepository.findByOrderGuid("order-guid")).thenReturn(order);
        when(inventoryFeignClient.checkInventoryAvailability(any())).thenReturn(ResponseEntity.ok(inventoryResponse));

        assertThatThrownBy(() ->
                orderService.checkoutOrder(
                        new CheckoutRequest("order-guid"),
                        "user-guid"
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("only 2 available");
    }
}
