package com.smartcommerce.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;

    @NotBlank(message = "{product.name.notblank}")
    @Size(min = 3, message = "{product.name.size}")
    private String productName;

    private String image;

    @NotBlank(message = "{product.description.notblank}")
    @Size(min = 6, message = "{product.description.size}")
    private String description;

    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}
