package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.CategoryModel;

import java.util.List;

public interface ICategoryPersistencePort {
    CategoryModel saveCategory(CategoryModel categoryModel);
    CategoryModel getCategoryById(Long idCategory);
    void deleteCategoryById(Long idCategory);
    List<CategoryModel> getAllCategories();
}
