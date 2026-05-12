package com.company.productservice.service;

import com.company.productservice.dto.CreateProductRequest;
import com.company.productservice.dto.PaginatedResponse;
import com.company.productservice.dto.ProductResponse;
import com.company.productservice.dto.UpdateProductRequest;
import com.company.productservice.exception.EntityExistsException;
import com.company.productservice.exception.EntityNotFoundException;
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
    public ProductService(ProductRepository productRepository, GuidService guidService){
        this.productRepository = productRepository;
        this.guidService = guidService;
    }
    public void createProduct(CreateProductRequest request, String userGuid){
        if(productRepository.findProductByProductName(request.getProductName())!=null){
            throw new EntityExistsException("Product", "Product exists already.");
        }
        Product product = new Product();
        product.setGuid(guidService.generateUUID());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setCreatedBy(userGuid);
        product.setStatus(StatusEnum.A);
        product.setPricePerUnit(request.getPricePerUnit());
        product.setLastUpdatedBy(userGuid);
        productRepository.save(product);
    }

    public PaginatedResponse<ProductResponse> getPaginatedProducts(int page, int size, String search){
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage;
        if(search!=null && !search.trim().isEmpty()){
            productPage = productRepository.findByProductNameOrDescriptionIgnoreCaseAndStatusIn(search,
            search, Collections.singleton(StatusEnum.A), pageable);
        } else{
            productPage = productRepository.findByStatusIn(Collections.singleton(StatusEnum.A), pageable);
        }
        List<ProductResponse> data = productPage
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();
        return new PaginatedResponse<>(
                data,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalPages()
        );
    }

    public ProductResponse getProductByGuid(String guid){
        Product product = productRepository.findProductByGuid(guid);
        if(product==null){
            throw new EntityNotFoundException("Product","Product not found.");
        }
        return mapToResponse(product);
    }

    public Map<String, Object> getProductsByGuids(List<String> guids){
        Map<String, Object> result = new HashMap<>();
        if(guids==null || guids.isEmpty()){
            result.put("products",Collections.emptyList());
            result.put("missingGuids",Collections.emptyList());
            return result;
        }
        Set<String> uniqueGuids = new HashSet<>(guids);
        List<Product> products = productRepository.findByGuidIn(uniqueGuids);
        List<ProductResponse> productResponses = products.stream()
                .map(this::mapToResponse)
                .toList();
        Set<String> foundGuids = products.stream()
                .map(Product::getGuid)
                .collect(Collectors.toSet());
        List<String> missingGuids = uniqueGuids.stream()
                .filter(g->!foundGuids.contains(g))
                .toList();
        result.put("products", productResponses);
        result.put("missingGuids",missingGuids);
        return result;
    }

    public void updateProduct(String guid, UpdateProductRequest request, String userGuid){
        Product product = productRepository.findProductByGuid(guid);
        if(product==null){
            throw new EntityNotFoundException("Product","Product not found.");
        }
        if(request.getDescription()!=null){
            if(request.getDescription().trim().isEmpty()){
                throw new IllegalArgumentException("Product description cannot be empty.");
            }
            product.setDescription(request.getDescription());
        }
        if(request.getPricePerUnit()!=null){
            product.setPricePerUnit(request.getPricePerUnit());
        }
        product.setLastUpdatedBy(userGuid);
        productRepository.save(product);
    }

    public void deactivateProduct(String productGuid, String userGuid){
        Product product = productRepository.findProductByGuid(productGuid);
        if(product==null || product.getStatus()==StatusEnum.D){
            throw new EntityNotFoundException("Product","Product not found.");
        }
        product.setStatus(StatusEnum.D);
        product.setLastUpdatedBy(userGuid);
        productRepository.save(product);
    }

    public ProductResponse mapToResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setDescription(product.getDescription());
        response.setProductName(product.getProductName());
        response.setPricePerUnit(product.getPricePerUnit());
        response.setProductGuid(product.getGuid());
        response.setStatus(product.getStatus());
        return response;
    }
}
