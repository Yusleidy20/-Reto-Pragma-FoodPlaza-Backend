package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user-micro/foodplaza")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandlerPort dishHandlerPort;
    private final IUserFeignClient userFeignClient;
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    @PostMapping("/dish")
    public ResponseEntity<String> createDish(
            @Valid @RequestBody DishRequestDto dishRequestDto) {
        log.info("Data received in DishRequestDto: {}", dishRequestDto);

        // Validaciones iniciales
        if (dishRequestDto.getNameDish() == null || dishRequestDto.getNameDish().isEmpty()) {
            log.warn("Name not provided.");
            return ResponseEntity.badRequest().body("The name of the dish is required.");
        }
        if (dishRequestDto.getIdRestaurant() == null || dishRequestDto.getIdRestaurant() <= 0) {
            log.warn("Invalid restaurant ID.");
            return ResponseEntity.badRequest().body("The restaurant ID is invalid.");
        }

        try {
            dishHandlerPort.createDish(dishRequestDto);
            log.info("Dish registered successfully.");
            return new ResponseEntity<>("Dish created successfully.", HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            log.error("Error: Restaurant not found.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error registering Plato", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Dish.");
        }
    }
    @PutMapping("/dish/{id}")
    public ResponseEntity<String> updateDish(
            @PathVariable Long id,
            @RequestBody @Valid DishUpdateRequestDto dishUpdateDto) {
        log.info("Request to update dish with ID {}: {}", id, dishUpdateDto);

        // Llamar al handler para actualizar el plato
        dishHandlerPort.updateDish(id, dishUpdateDto);
        log.info("Dish updated successfully.");
        return ResponseEntity.ok("Dish updated successfully.");
    }


}
