package com.company.productservice.repository;

import com.company.productservice.model.Product;
import com.company.productservice.util.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private void createProduct(String guid, String name, String desc, StatusEnum status) {
        Product p = new Product();
        p.setGuid(guid);
        p.setProductName(name);
        p.setDescription(desc);
        p.setPricePerUnit(100.0);
        p.setStatus(status);
        productRepository.save(p);
    }

    @Test
    void shouldFindProductByProductName() {
        createProduct("g1", "laptop", "coding machine", StatusEnum.A);
        Product result = productRepository.findProductByProductName("laptop");
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("laptop");
    }

    @Test
    void shouldFindProductByGuid() {
        createProduct("g1", "laptop", "coding machine", StatusEnum.A);
        Product result = productRepository.findProductByGuid("g1");
        assertThat(result).isNotNull();
        assertThat(result.getGuid()).isEqualTo("g1");
    }

    @Test
    void shouldReturnProductsByStatusWithPagination() {
        createProduct("g1", "laptop", "desc1", StatusEnum.A);
        createProduct("g2", "mouse", "desc2", StatusEnum.D);
        Page<Product> result = productRepository.findByStatusIn(
                Set.of(StatusEnum.A),
                PageRequest.of(0, 10)
        );
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus())
                .isEqualTo(StatusEnum.A);
    }

    @Test
    void shouldReturnProductsByGuids() {
        createProduct("g1", "laptop", "desc", StatusEnum.A);
        createProduct("g2", "mouse", "desc", StatusEnum.A);
        List<Product> result = productRepository.findByGuidIn(List.of("g1", "g2"));
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldSearchByNameOrDescriptionIgnoreCaseAndStatus() {
        createProduct("g1", "Laptop", "Coding Machine", StatusEnum.A);
        createProduct("g2", "Mouse", "Gaming device", StatusEnum.A);
        createProduct("g3", "Laptop Stand", "Accessory", StatusEnum.D);

        Page<Product> result = productRepository
                .searchProducts(
                        "laptop",
                        Set.of(StatusEnum.A),
                        PageRequest.of(0, 10)
                );
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getProductName())
                .isEqualTo("Laptop");
    }
}