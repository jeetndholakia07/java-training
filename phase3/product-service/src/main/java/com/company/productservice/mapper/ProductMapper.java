package com.company.productservice.mapper;

import com.company.productservice.dto.request.CreateProductRequest;
import com.company.productservice.dto.request.UpdateProductRequest;
import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(CreateProductRequest request);

    @Mapping(source = "guid", target = "productGuid")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(
            UpdateProductRequest request,
            @MappingTarget Product entity
    );
}
