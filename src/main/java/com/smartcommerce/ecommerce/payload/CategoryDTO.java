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
public class CategoryDTO {
    Long categoryId;

    @NotBlank(message = "{category.name.notblank}")
    @Size(min = 5, message = "{category.name.size}")
    String categoryName;
}
