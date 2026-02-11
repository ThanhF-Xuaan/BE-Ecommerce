package com.smartcommerce.ecommerce.controller;

import com.smartcommerce.ecommerce.config.AppConstants;
import com.smartcommerce.ecommerce.payload.CategoryDTO;
import com.smartcommerce.ecommerce.payload.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Category", description = "Quản lý danh mục sản phẩm")
public interface CategoryApi {
    @Operation(summary = "Lấy danh sách danh mục", description = "Lấy danh sách có phân trang và sắp xếp (Public).")
    ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    );

    @Operation(summary = "Tạo danh mục mới", description = "Admin tạo danh mục sản phẩm mới.")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO);

    @Operation(summary = "Xóa danh mục", description = "Admin xóa danh mục theo ID.")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId);

    @Operation(summary = "Cập nhật danh mục", description = "Admin cập nhật tên danh mục.")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
                                               @Valid @RequestBody CategoryDTO categoryDTO);
}
