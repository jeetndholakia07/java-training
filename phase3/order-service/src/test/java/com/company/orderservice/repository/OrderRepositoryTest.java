package com.company.orderservice.repository;

import com.company.orderservice.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import jakarta.persistence.EntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find orders by user guid")
    void findByUserGuid() {
        Order order1 = new Order();
        order1.setUserGuid("user-1");
        order1.setOrderGuid("order-1");

        Order order2 = new Order();
        order2.setUserGuid("user-1");
        order2.setOrderGuid("order-2");

        Order order3 = new Order();
        order3.setUserGuid("user-2");
        order3.setOrderGuid("order-3");

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(order3);
        entityManager.flush();

        Page<Order> result = orderRepository.findByUserGuid(
                "user-1",
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Order::getOrderGuid)
                .containsExactlyInAnyOrder("order-1", "order-2");
    }

    @Test
    @DisplayName("Should find order by order guid")
    void findByOrderGuid() {
        Order order = new Order();
        order.setUserGuid("user-1");
        order.setOrderGuid("order-123");

        entityManager.persist(order);
        entityManager.flush();
        Order result = orderRepository.findByOrderGuid("order-123");
        assertThat(result).isNotNull();
        assertThat(result.getOrderGuid()).isEqualTo("order-123");
    }
}