package com.example.foodplaza.domain.api;

import com.example.foodplaza.domain.model.CategoryModel;

import java.util.List;

public interface ICategoryServicePort {
    void createCategory(CategoryModel categoryModel);
    CategoryModel getCategoryById(Long idCategory);
    void deleteCategory(Long idCategory);
    List<CategoryModel> getAllCategories();
}
