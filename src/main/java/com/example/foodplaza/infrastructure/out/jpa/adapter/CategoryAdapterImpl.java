package com.example.foodplaza.infrastructure.out.jpa.adapter;


import com.example.foodplaza.domain.exception.NotdataFoundException;
import com.example.foodplaza.domain.model.CategoryModel;
import com.example.foodplaza.domain.spi.persistence.ICategoryPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.CategoryEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.ICategoryEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryAdapterImpl implements ICategoryPersistencePort {
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;


    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {
        CategoryEntity categoryEntity = categoryRepository.save(categoryEntityMapper.toCategoryEntity(categoryModel));
        return categoryEntityMapper.toCategoryModel(categoryEntity);
    }

    @Override
    public CategoryModel getCategoryById(Long idCategory) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(idCategory);
        CategoryEntity categoryEntity = categoryEntityOptional.orElse(null);
        return categoryEntityMapper.toCategoryModel(categoryEntity);
    }

    @Override
    public void deleteCategoryById(Long idCategory) {
        categoryRepository.deleteById(idCategory);
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        if(categoryEntities.isEmpty()){
            throw new NotdataFoundException();
        }
        return categoryEntityMapper.toCategoryModelList(categoryEntities);
    }


}
