package com.smartcommerce.ecommerce.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDTO {
    Long cartItemId;
    CartDTO cart;
    ProductDTO productDTO;
    Integer quantity;
    Double discount;
    Double productPrice;
}
