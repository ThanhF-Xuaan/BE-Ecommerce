package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
