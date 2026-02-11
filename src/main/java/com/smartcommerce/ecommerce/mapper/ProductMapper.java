package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Product;
import com.smartcommerce.ecommerce.payload.ProductDTO;
import com.smartcommerce.ecommerce.payload.ProductResponse;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductDTO toDTO(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);

    default ProductResponse toProductResponse(Page<Product> productPage) {
        List<ProductDTO> content = productPage.getContent().stream()
                .map(this::toDTO)
                .toList();

        return ProductResponse.builder()
                .content(content)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }
}
