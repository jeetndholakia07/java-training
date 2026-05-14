package com.company.productservice.repository;

import com.company.productservice.model.Product;
import com.company.productservice.util.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductByProductName(String productName);

    Product findProductByGuid(String guid);

    @Query("""
            SELECT p FROM Product p
            WHERE (LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
            AND p.status IN :statuses
            """)
    Page<Product> searchProducts(
            @Param("search") String search,
            @Param("statuses") Collection<StatusEnum> statuses,
            Pageable pageable
    );

    List<Product> findByGuidIn(Collection<String> guids);

    Page<Product> findByStatusIn(Collection<StatusEnum> statuses, Pageable pageable);
}
