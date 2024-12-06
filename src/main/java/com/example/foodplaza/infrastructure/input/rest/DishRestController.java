package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishListResponseDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user-micro/foodplaza")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandlerPort dishHandlerPort;
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    // Endpoint para crear el plato
    @PostMapping("/dish")
    @PreAuthorize("hasAuthority('Owner')")
    public ResponseEntity<String> createDish(@Valid @RequestBody DishRequestDto dishRequestDto) throws IllegalAccessException {
        log.info("Data received in DishRequestDto: {}", dishRequestDto);

        dishHandlerPort.createDish(dishRequestDto);
        log.info("Dish registered successfully.");
        return new ResponseEntity<>("Dish created successfully.", HttpStatus.CREATED);
    }

    // Endpoint para actualizar el plato
    @PutMapping("/dish/{id}")
    @PreAuthorize("hasAuthority('Owner')")
    public ResponseEntity<String> updateDish(
            @PathVariable Long id,
            @RequestBody @Valid DishUpdateRequestDto dishUpdateDto) {
        log.info("Request to update dish with ID {}: {}", id, dishUpdateDto);

        dishHandlerPort.updateDish(id, dishUpdateDto);
        log.info("Dish updated successfully.");
        return ResponseEntity.ok("Dish updated successfully.");
    }

    @PreAuthorize("hasRole('Customer')")
    @GetMapping("/listDish")
    public ResponseEntity<List<DishListResponseDto>> listDishes(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long idCategory,
            @RequestParam Long idRestaurant) {

        // Crear parámetros de paginación
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // Llamar al handler para obtener los platos
        List<DishListResponseDto> dishes = dishHandlerPort.listDishes(pageRequest, idCategory, idRestaurant);

        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }






}
