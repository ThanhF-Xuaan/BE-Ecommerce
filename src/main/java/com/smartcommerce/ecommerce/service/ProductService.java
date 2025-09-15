package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.payload.ProductDTO;
import com.smartcommerce.ecommerce.payload.ProductResponse;
import jakarta.validation.Valid;

public interface ProductService {
    ProductDTO createProduct(Long categoryId, @Valid ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getAllProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
}
