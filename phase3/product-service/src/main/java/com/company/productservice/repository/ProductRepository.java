package com.company.productservice.repository;

import com.company.productservice.model.Product;
import com.company.productservice.util.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findProductByProductName(String productName);
    Product findProductByGuid(String guid);
    Page<Product> findProductsByProductNameAndDescriptionIgnoreCaseAndStatusIn(String productName, String description, Collection<StatusEnum> statuses, Pageable pageable);
    List<Product> findByGuidIn(Collection<String> guids);
}
