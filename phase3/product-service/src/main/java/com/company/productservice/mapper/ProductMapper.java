package com.company.productservice.mapper;

import com.company.productservice.dto.request.CreateProductRequest;
import com.company.productservice.dto.request.UpdateProductRequest;
import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(CreateProductRequest request);
    ProductResponse toResponse(Product product);
    List<ProductResponse> toResponseList(List<Product> products);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(
            UpdateProductRequest request,
            @MappingTarget Product entity
    );
}
