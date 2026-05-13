package com.company.productservice.repository;

import com.company.productservice.model.Product;
import com.company.productservice.util.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldFindProductByName() {
        Product product = new Product();
        product.setGuid(UUID.randomUUID().toString());
        product.setProductName("Laptop");
        product.setDescription("Gaming Laptop");
        product.setPricePerUnit(1000.0);
        product.setStatus(StatusEnum.A);

        productRepository.save(product);

        Product found =
                productRepository.findProductByProductName("Laptop");

        assertNotNull(found);
        assertEquals("Laptop", found.getProductName());
    }

    @Test
    void shouldFindProductByGuid() {
        Product product = new Product();
        product.setGuid("guid-123");
        product.setProductName("Phone");
        product.setStatus(StatusEnum.A);

        productRepository.save(product);

        Product found =
                productRepository.findProductByGuid("guid-123");

        assertNotNull(found);
        assertEquals("guid-123", found.getGuid());
    }

    @Test
    void shouldFindProductsByGuidIn() {
        Product p1 = new Product();
        p1.setGuid("g1");
        p1.setProductName("P1");
        p1.setStatus(StatusEnum.A);

        Product p2 = new Product();
        p2.setGuid("g2");
        p2.setProductName("P2");
        p2.setStatus(StatusEnum.A);

        productRepository.saveAll(List.of(p1, p2));

        List<Product> products =
                productRepository.findByGuidIn(List.of("g1", "g2"));

        assertEquals(2, products.size());
    }
}