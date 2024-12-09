package com.example.foodplaza.domain.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.foodplaza.domain.model.CategoryModel;
import com.example.foodplaza.domain.spi.persistence.ICategoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    private CategoryUseCase categoryUseCase;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
        categoryUseCase = new CategoryUseCase(categoryPersistencePort);
    }

    @Test
    void testCreateCategory() {
        // Arrange
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setNameCategory("Category 1");

        // Act
        categoryUseCase.createCategory(categoryModel);

        // Assert
        verify(categoryPersistencePort, times(1)).saveCategory(categoryModel);
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        Long categoryId = 1L;
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setIdCategory(categoryId);
        categoryModel.setNameCategory("Category 1");

        when(categoryPersistencePort.getCategoryById(categoryId)).thenReturn(categoryModel);

        // Act
        CategoryModel result = categoryUseCase.getCategoryById(categoryId);

        // Assert
        assertEquals(categoryModel, result);
        verify(categoryPersistencePort, times(1)).getCategoryById(categoryId);
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        Long categoryId = 1L;

        // Act
        categoryUseCase.deleteCategory(categoryId);

        // Assert
        verify(categoryPersistencePort, times(1)).deleteCategoryById(categoryId);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        CategoryModel category1 = new CategoryModel();
        category1.setNameCategory("Category 1");
        CategoryModel category2 = new CategoryModel();
        category2.setNameCategory("Category 2");

        List<CategoryModel> expectedCategories = Arrays.asList(category1, category2);
        when(categoryPersistencePort.getAllCategories()).thenReturn(expectedCategories);

        // Act
        List<CategoryModel> result = categoryUseCase.getAllCategories();

        // Assert
        assertEquals(expectedCategories, result);
        verify(categoryPersistencePort, times(1)).getAllCategories();
    }
}
