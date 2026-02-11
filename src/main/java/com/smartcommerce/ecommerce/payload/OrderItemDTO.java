package com.smartcommerce.ecommerce.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemDTO {
    Long orderItemId;
    ProductDTO product;
    Integer quantity;
    Double discount;
    Double orderedProductPrice;
}
