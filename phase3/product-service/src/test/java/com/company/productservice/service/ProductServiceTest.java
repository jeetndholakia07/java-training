package com.company.productservice.service;

import com.company.productservice.dto.request.CreateProductRequest;
import com.company.productservice.dto.request.UpdateProductRequest;
import com.company.productservice.dto.response.PaginatedResponse;
import com.company.productservice.dto.response.ProductListResponse;
import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.exception.EntityExistsException;
import com.company.productservice.exception.EntityNotFoundException;
import com.company.productservice.mapper.ProductMapper;
import com.company.productservice.mapper.ProductMapperImpl;
import com.company.productservice.model.Product;
import com.company.productservice.repository.ProductRepository;
import com.company.productservice.util.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private GuidService guidService;
    private ProductService productService;

    private Product product;
    private String userGuid;
    private ProductMapper productMapper;

    @BeforeEach
    void setup(){
        product = new Product();
        product.setGuid("guid-1");
        product.setProductName("laptop");
        product.setDescription("coding");
        product.setPricePerUnit(100.0);
        product.setStatus(StatusEnum.A);
        userGuid = "user-guid";
        productMapper = new ProductMapperImpl();
        productService = new ProductService(
                productRepository,
                guidService,
                productMapper
        );
    }

    @Test
    @DisplayName("Should throw exception when creating a product that already exists")
    void createProduct_ProductExists(){
        CreateProductRequest request = new CreateProductRequest("laptop","coding",100.0);
        when(productRepository.findProductByProductName(request.productName())).thenReturn(product);
        assertThatThrownBy(()->productService.createProduct(request, userGuid))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Product exists already.");
        verify(productRepository).findProductByProductName(product.getProductName());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should create and save product when product does not already exist")
    void createProduct_ProductNotExists(){
        CreateProductRequest request = new CreateProductRequest("laptop","coding",100.0);
        when(productRepository.findProductByProductName(request.productName())).thenReturn(null);
        when(guidService.generateUUID()).thenReturn("guid-1");
        productService.createProduct(request, userGuid);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getProductName()).isEqualTo(request.productName());
        assertThat(captor.getValue().getDescription()).isEqualTo(request.description());
        assertThat(captor.getValue().getPricePerUnit()).isEqualTo(request.pricePerUnit());
        assertThat(captor.getValue().getCreatedBy()).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("Should throw exception when product is not found by guid")
    void getProduct_NotFound(){
        String productGuid = "guid-xyz";
        when(productRepository.findProductByGuid(productGuid)).thenReturn(null);
        assertThatThrownBy(()->productService.getProductByGuid(productGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product not found.");
    }

    @Test
    @DisplayName("Should return product response when product exists")
    void getProduct_Found(){
        String productGuid = "guid-1";
        when(productRepository.findProductByGuid(productGuid)).thenReturn(product);
        ProductResponse productResponse = productService.getProductByGuid(productGuid);
        assertThat(productResponse.productGuid()).isEqualTo(productGuid);
    }

    @Test
    @DisplayName("Should throw exception when updating non existing product")
    void updateProduct_NotFound(){
        String productGuid = "guid-xyz";
        UpdateProductRequest request = new UpdateProductRequest("mobile",500.0);
        when(productRepository.findProductByGuid(productGuid)).thenReturn(null);
        assertThatThrownBy(()->productService.updateProduct(productGuid,request, userGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product not found.");
    }

    @Test
    @DisplayName("Should update existing product details successfully")
    void updateProduct_Found(){
        String productGuid = "guid-1";
        UpdateProductRequest request = new UpdateProductRequest("development",100.0);
        when(productRepository.findProductByGuid(productGuid)).thenReturn(product);
        productService.updateProduct(productGuid, request, userGuid);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getDescription()).isEqualTo(request.description());
        assertThat(captor.getValue().getPricePerUnit()).isEqualTo(request.pricePerUnit());
        assertThat(captor.getValue().getLastUpdatedBy()).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("Should throw exception when deactivating non existing product")
    void deleteProduct_NotFound(){
        String productGuid = "guid-xyz";
        when(productRepository.findProductByGuid(productGuid)).thenReturn(null);
        assertThatThrownBy(()->productService.deactivateProduct(productGuid, userGuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product not found.");
    }

    @Test
    @DisplayName("Should deactivate existing product successfully")
    void deleteProduct_Exists(){
        String productGuid = "guid-1";
        when(productRepository.findProductByGuid(productGuid)).thenReturn(product);
        productService.deactivateProduct(productGuid, userGuid);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(StatusEnum.D);
        assertThat(captor.getValue().getLastUpdatedBy()).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("Should return paginated active products when search is not provided")
    void getPaginatedProducts_withoutSearch(){
        Page<Product> page = new PageImpl<>(
                List.of(product),
                PageRequest.of(0,5),
                1
        );
        when(productRepository.findByStatusIn(
                eq(Set.of(StatusEnum.A)),
                any(Pageable.class))
        ).thenReturn(page);
        PaginatedResponse<ProductResponse> result = productService.getPaginatedProducts(0,5,null);
        assertThat(result.data()).hasSize(1);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.totalPages()).isEqualTo(1);
        verify(productRepository).findByStatusIn(
                eq(Set.of(StatusEnum.A)),
                any(Pageable.class));

        verify(productRepository, never())
                .searchProducts(
                        anyString(),
                        any(),
                        any(Pageable.class)
                );
    }

    @Test
    @DisplayName("Should return paginated filtered products when search term is provided")
    void getPaginatedProducts_withSearch(){
        String search = "laptop";
        Page<Product> page = new PageImpl<>(
                List.of(product),
                PageRequest.of(0, 5),
                1
        );
        when(productRepository
                .searchProducts(
                        eq(search),
                        eq(Set.of(StatusEnum.A)),
                        any(Pageable.class)
                )).thenReturn(page);
        PaginatedResponse<ProductResponse> result =
                productService.getPaginatedProducts(0, 5, search);
        assertThat(result.data()).hasSize(1);
        assertThat(result.data().get(0).productGuid())
                .isEqualTo(product.getGuid());
        verify(productRepository)
                .searchProducts(
                        eq(search),
                        eq(Set.of(StatusEnum.A)),
                        any(Pageable.class)
                );
    }

    @Test
    @DisplayName("Should return empty paginated response when no products exist")
    void getPaginatedProducts_emptyResult(){
        Page<Product> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10),
                0
        );
        when(productRepository.findByStatusIn(
                eq(Set.of(StatusEnum.A)),
                any(Pageable.class))
        ).thenReturn(emptyPage);
        PaginatedResponse<ProductResponse> result =
                productService.getPaginatedProducts(0, 10, null);
        assertThat(result.data()).isEmpty();
        assertThat(result.totalPages()).isZero();
    }

    @Test
    @DisplayName("Should return empty product list when guid list is null")
    void getProductByGuids_NullInput(){
        ProductListResponse response = productService.getProductsByGuids(null);
        assertThat(response.products()).isEqualTo(List.of());
        assertThat(response.missingGuids()).isEqualTo(List.of());
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("Should return empty product list when guid list is null")
    void getProductByGuids_EmptyInput(){
        ProductListResponse response = productService.getProductsByGuids(List.of());
        assertThat(response.products()).isEqualTo(List.of());
        assertThat(response.missingGuids()).isEqualTo(List.of());
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("Should return all matching products when all guids exist")
    void getProductByGuids_Result(){
        List<String> guids = List.of("guid-1");
        when(productRepository.findByGuidIn(Set.of("guid-1")))
                .thenReturn(List.of(product));
        ProductListResponse response = productService.getProductsByGuids(guids);
        assertThat(response.products()).hasSize(1);
        assertThat(response.products().get(0).productGuid()).isEqualTo(product.getGuid());
        assertThat(response.missingGuids()).isEmpty();
    }

    @Test
    @DisplayName("Should ignore duplicate guids when fetching products")
    void getProductByGuids_duplicateGuids(){
        List<String> guids = List.of("guid-1", "guid-1");
        when(productRepository.findByGuidIn(Set.of("guid-1")))
                .thenReturn(List.of(product));
        ProductListResponse response = productService.getProductsByGuids(guids);
        assertThat(response.products()).hasSize(1);
        assertThat(response.products().get(0).productGuid()).isEqualTo(product.getGuid());
        assertThat(response.missingGuids()).isEmpty();
    }

    @Test
    @DisplayName("Should return missing guids when some products do not exist")
    void getProductByGuids_someMissing(){
        List<String> guids = List.of("guid-1", "guid-2");
        when(productRepository.findByGuidIn(Set.of("guid-1", "guid-2")))
                .thenReturn(List.of(product));
        ProductListResponse response = productService.getProductsByGuids(guids);
        assertThat(response.products()).hasSize(1);
        assertThat(response.products().get(0).productGuid()).isEqualTo(product.getGuid());
        assertThat(response.missingGuids()).containsExactly("guid-2");
    }
}
