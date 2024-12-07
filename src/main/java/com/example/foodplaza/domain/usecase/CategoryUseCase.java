package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.api.ICategoryServicePort;
import com.example.foodplaza.domain.model.CategoryModel;
import com.example.foodplaza.domain.spi.persistence.ICategoryPersistencePort;

import java.util.List;

public class CategoryUseCase implements ICategoryServicePort {
    private final ICategoryPersistencePort categoryPersistencePort;

    public CategoryUseCase(ICategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }



    @Override
    public void createCategory(CategoryModel categoryModel) {
        categoryPersistencePort.saveCategory(categoryModel);
    }

    @Override
    public CategoryModel getCategoryById(Long idCategory) {
        return categoryPersistencePort.getCategoryById(idCategory);
    }

    @Override
    public void deleteCategory(Long idCategory) {
        categoryPersistencePort.deleteCategoryById(idCategory);
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoryPersistencePort.getAllCategories();
    }
}
