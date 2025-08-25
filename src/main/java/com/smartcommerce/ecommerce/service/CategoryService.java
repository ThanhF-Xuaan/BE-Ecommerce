package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    void updateCategory(Long categoryId, Category category);
}
