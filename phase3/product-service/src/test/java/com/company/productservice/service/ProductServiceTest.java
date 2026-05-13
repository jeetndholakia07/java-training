package com.company.productservice.service;

import com.company.productservice.dto.request.CreateProductRequest;
import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.dto.request.UpdateProductRequest;
import com.company.productservice.exception.EntityExistsException;
import com.company.productservice.exception.EntityNotFoundException;
import com.company.productservice.model.Product;
import com.company.productservice.repository.ProductRepository;
import com.company.productservice.util.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private GuidService guidService;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setGuid("guid-1");
        product.setProductName("Laptop");
        product.setDescription("Gaming");
        product.setPricePerUnit(1000.0);
        product.setStatus(StatusEnum.A);
    }

    @Test
    void shouldCreateProduct() {
        CreateProductRequest request = new CreateProductRequest("Laptop","Gaming",1000.0);

        when(productRepository.findProductByProductName("Laptop"))
                .thenReturn(null);

        when(guidService.generateUUID())
                .thenReturn("guid-1");

        productService.createProduct(request, "user-guid");

        verify(productRepository, times(1))
                .save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductExists() {
        CreateProductRequest request = new CreateProductRequest("Laptop","Gaming",1000.0);

        when(productRepository.findProductByProductName("Laptop"))
                .thenReturn(product);

        assertThrows(EntityExistsException.class,
                () -> productService.createProduct(request, "user"));
    }

    @Test
    void shouldGetProductByGuid() {
        when(productRepository.findProductByGuid("guid-1"))
                .thenReturn(product);

        ProductResponse response =
                productService.getProductByGuid("guid-1");

        assertEquals("Laptop", response.getProductName());
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(productRepository.findProductByGuid("x"))
                .thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> productService.getProductByGuid("x"));
    }

    @Test
    void shouldUpdateProduct() {
        UpdateProductRequest request = new UpdateProductRequest("Updated",2000.0);

        when(productRepository.findProductByGuid("guid-1"))
                .thenReturn(product);

        productService.updateProduct("guid-1",
                request,
                "admin");

        verify(productRepository).save(product);

        assertEquals("Updated", product.getDescription());
        assertEquals(2000.0, product.getPricePerUnit());
    }

    @Test
    void shouldDeactivateProduct() {
        when(productRepository.findProductByGuid("guid-1"))
                .thenReturn(product);

        productService.deactivateProduct("guid-1", "admin");

        assertEquals(StatusEnum.D, product.getStatus());

        verify(productRepository).save(product);
    }

    @Test
    void shouldGetProductsByGuids() {
        when(productRepository.findByGuidIn(any()))
                .thenReturn(List.of(product));

        var result =
                productService.getProductsByGuids(
                        List.of("guid-1"));

        List<?> products =
                (List<?>) result.get("products");

        assertEquals(1, products.size());
    }
}