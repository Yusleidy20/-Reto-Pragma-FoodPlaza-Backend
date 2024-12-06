package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.CategoryRequestDto;
import com.example.foodplaza.application.dto.response.CategoryResponseDto;
import com.example.foodplaza.application.handler.ICategoryHandler;
import com.example.foodplaza.application.mapper.request.ICategoryRequestMapper;
import com.example.foodplaza.application.mapper.response.ICategoryResponseMapper;
import com.example.foodplaza.domain.api.ICategoryServicePort;
import com.example.foodplaza.domain.exception.NotdataFoundException;
import com.example.foodplaza.domain.model.CategoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryHandlerImpl implements ICategoryHandler {
    private final ICategoryServicePort categoryServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;
    private  final ICategoryResponseMapper categoryResponseMapper;

    @Override
    public void saveCategory(CategoryRequestDto categoryRequestDto) {
        CategoryModel categoryModel = categoryRequestMapper.toCategory(categoryRequestDto);
        categoryServicePort.createCategory(categoryModel);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long idCategory) {
        CategoryModel categoryModel = categoryServicePort.getCategoryById(idCategory);
        return categoryResponseMapper.toResponse(categoryModel);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryModel> categoryModelList = categoryServicePort.getAllCategories();
        if(categoryModelList.isEmpty()){
            throw new NotdataFoundException();
        }
        return categoryResponseMapper.toCategoryResponseList(categoryModelList);
    }

    @Override
    public void deleteCategoryById(Long idCategory) {
        categoryServicePort.deleteCategory(idCategory);
    }
}
