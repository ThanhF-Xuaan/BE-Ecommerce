package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.exceptions.ResourceNotFoundException;
import com.smartcommerce.ecommerce.model.Category;
import com.smartcommerce.ecommerce.model.Product;
import com.smartcommerce.ecommerce.payload.ProductDTO;
import com.smartcommerce.ecommerce.payload.ProductResponse;
import com.smartcommerce.ecommerce.repositories.CategoryRepository;
import com.smartcommerce.ecommerce.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
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
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(CategoryRepository categoryRepository,
                              ProductRepository productRepository,
                              FileService fileService,
                              ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO createProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        for(Product value : category.getProducts()) {
            if(value.getProductName().equals(productDTO.getProductName())) {
                throw new APIException("Product already exists");
            }
        }

        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();

        if(products.isEmpty()) {
            throw new APIException("No products exist");
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return new ProductResponse(productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast());
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId,
                                                    Integer pageNumber,
                                                    Integer pageSize,
                                                    String sortBy,
                                                    String sortOrder) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            throw new APIException("Products not found with category: " + categoryId);
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return new ProductResponse(productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast());
    }

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword,
                                                   Integer pageNumber,
                                                   Integer pageSize,
                                                   String sortBy,
                                                   String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository
                .findByProductNameLikeIgnoreCase("%" + keyword + "%", pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            throw new APIException("Products not found with keyword: " + keyword);
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return new ProductResponse(productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast());
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB =  productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(productDTO, Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setSpecialPrice(product.getSpecialPrice());

        return modelMapper.map(productRepository.save(productFromDB), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }
}
