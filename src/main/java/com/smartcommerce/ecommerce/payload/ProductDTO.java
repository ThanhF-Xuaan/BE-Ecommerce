package com.smartcommerce.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    Long productId;

    @NotBlank(message = "{product.name.notblank}")
    @Size(min = 3, message = "{product.name.size}")
    String productName;

    String image;

    @NotBlank(message = "{product.description.notblank}")
    @Size(min = 6, message = "{product.description.size}")
    String description;

    Integer quantity;
    Double price;
    Double discount;
    Double specialPrice;
}
