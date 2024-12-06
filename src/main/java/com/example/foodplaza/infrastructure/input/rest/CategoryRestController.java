package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.CategoryRequestDto;
import com.example.foodplaza.application.dto.response.CategoryResponseDto;
import com.example.foodplaza.application.handler.ICategoryHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user-micro/foodplaza")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ICategoryHandler categoryHandler;

    // Crear una nueva categoría
    @PostMapping("/category")
    public ResponseEntity<String> saveCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        System.out.println("Received category: " + categoryRequestDto);  // Para depuración
        categoryHandler.saveCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
    }


    // Obtener una categoría por ID
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto categoryResponseDto = categoryHandler.getCategoryById(id);
        return ResponseEntity.ok(categoryResponseDto);
    }

    // Obtener todas las categorías
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryResponseDtos = categoryHandler.getAllCategories();
        return ResponseEntity.ok(categoryResponseDtos); // Devuelve 200 OK con la lista de DTOs
    }

    // Eliminar una categoría por ID
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryHandler.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Devuelve 204 No Content (sin contenido)
    }
}
