package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByProductNameLikeIgnoreCase(String productName, Pageable pageable);

    Page<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);
}
