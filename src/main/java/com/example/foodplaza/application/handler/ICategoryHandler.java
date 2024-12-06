package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.CategoryRequestDto;
import com.example.foodplaza.application.dto.response.CategoryResponseDto;

import java.util.List;

public interface ICategoryHandler {
    void saveCategory(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto getCategoryById(Long idCategory);
    List<CategoryResponseDto> getAllCategories();
    void deleteCategoryById(Long idCategory);
}
