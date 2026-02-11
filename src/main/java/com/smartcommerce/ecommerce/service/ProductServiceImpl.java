package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.exceptions.ResourceNotFoundException;
import com.smartcommerce.ecommerce.mapper.ProductMapper;
import com.smartcommerce.ecommerce.model.Cart;
import com.smartcommerce.ecommerce.model.Category;
import com.smartcommerce.ecommerce.model.Product;
import com.smartcommerce.ecommerce.payload.ProductDTO;
import com.smartcommerce.ecommerce.payload.ProductResponse;
import com.smartcommerce.ecommerce.repositories.CartRepository;
import com.smartcommerce.ecommerce.repositories.CategoryRepository;
import com.smartcommerce.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;
    CartService cartService;
    FileService fileService;
    ProductMapper productMapper;

    @NonFinal
    @Value("${project.image}")
    String path;

    private Double calculateSpecialPrice(Double price, Double discount) {
        return price - ((discount * 0.01) * price);
    }

    private Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isDuplicate = category.getProducts().stream()
                .anyMatch(p -> p.getProductName().equalsIgnoreCase(productDTO.getProductName()));

        if (isDuplicate) throw new APIException("Product already exists in this category");

        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        product.setImage("default.png");
        product.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));

        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productMapper.toProductResponse(productPage);
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId,
                                                    Integer pageNumber,
                                                    Integer pageSize,
                                                    String sortBy,
                                                    String sortOrder) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        }

        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findByCategory_CategoryId(categoryId, pageable);
        return productMapper.toProductResponse(productPage);
    }

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword,
                                                   Integer pageNumber,
                                                   Integer pageSize,
                                                   String sortBy,
                                                   String sortOrder) {
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageable);
        return productMapper.toProductResponse(productPage);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productMapper.updateProductFromDTO(productDTO, productFromDB);
        productFromDB.setSpecialPrice(calculateSpecialPrice(productFromDB.getPrice(), productFromDB.getDiscount()));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return productMapper.toDTO(productRepository.save(productFromDB));
    }

    @Override
    @Transactional
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);
        return productMapper.toDTO(productRepository.save(product));
    }
}
