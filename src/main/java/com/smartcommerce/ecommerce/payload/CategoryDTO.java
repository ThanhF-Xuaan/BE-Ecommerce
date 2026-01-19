package com.smartcommerce.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;

    @NotBlank(message = "{category.name.notblank}")
    @Size(min = 5, message = "{category.name.size}")
    private String categoryName;
}
