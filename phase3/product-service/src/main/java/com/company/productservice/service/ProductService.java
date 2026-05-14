package com.company.productservice.service;

import com.company.productservice.dto.request.CreateProductRequest;
import com.company.productservice.dto.response.PaginatedResponse;
import com.company.productservice.dto.response.ProductListResponse;
import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.dto.request.UpdateProductRequest;
import com.company.productservice.exception.EntityExistsException;
import com.company.productservice.exception.EntityNotFoundException;
import com.company.productservice.mapper.ProductMapper;
import com.company.productservice.model.Product;
import com.company.productservice.repository.ProductRepository;
import com.company.productservice.util.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final GuidService guidService;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, GuidService guidService, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.guidService = guidService;
        this.productMapper = mapper;
    }

    public void createProduct(CreateProductRequest request, String userGuid) {
        if (productRepository.findProductByProductName(request.productName()) != null) {
            throw new EntityExistsException("Product", "Product exists already.");
        }
        Product product = productMapper.toEntity(request);
        product.setGuid(guidService.generateUUID());
        product.setCreatedBy(userGuid);
        product.setStatus(StatusEnum.A);
        product.setLastUpdatedBy(userGuid);
        productRepository.save(product);
    }

    public PaginatedResponse<ProductResponse> getPaginatedProducts(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        if (search != null && !search.trim().isEmpty()) {
            productPage = productRepository.searchProducts(search,
            Collections.singleton(StatusEnum.A), pageable);
        } else {
            productPage = productRepository.findByStatusIn(Collections.singleton(StatusEnum.A), pageable);
        }
        List<ProductResponse> data = productMapper.toResponseList(productPage
                .getContent()
                .stream()
                .toList());
        return new PaginatedResponse<>(
                data,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalPages()
        );
    }

    public ProductResponse getProductByGuid(String guid) {
        Product product = productRepository.findProductByGuid(guid);
        if (product == null) {
            throw new EntityNotFoundException("Product", "Product not found.");
        }
        return productMapper.toResponse(product);
    }

    public ProductListResponse getProductsByGuids(List<String> guids) {
        if (guids == null || guids.isEmpty()) {
            return new ProductListResponse(
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }
        Set<String> uniqueGuids = new HashSet<>(guids);
        List<Product> products = productRepository.findByGuidIn(uniqueGuids);
        List<ProductResponse> productResponses = products.stream()
                .map(productMapper::toResponse)
                .toList();
        Set<String> foundGuids = products.stream()
                .map(Product::getGuid)
                .collect(Collectors.toSet());
        List<String> missingGuids = uniqueGuids.stream()
                .filter(g -> !foundGuids.contains(g))
                .toList();
        return new ProductListResponse(productResponses, missingGuids);
    }

    public void updateProduct(String guid, UpdateProductRequest request, String userGuid) {
        Product product = productRepository.findProductByGuid(guid);
        if (product == null) {
            throw new EntityNotFoundException("Product", "Product not found.");
        }
        productMapper.updateEntityFromDto(request, product);
        product.setLastUpdatedBy(userGuid);
        productRepository.save(product);
    }

    public void deactivateProduct(String productGuid, String userGuid) {
        Product product = productRepository.findProductByGuid(productGuid);
        if (product == null || product.getStatus() == StatusEnum.D) {
            throw new EntityNotFoundException("Product", "Product not found.");
        }
        product.setStatus(StatusEnum.D);
        product.setLastUpdatedBy(userGuid);
        productRepository.save(product);
    }
}
