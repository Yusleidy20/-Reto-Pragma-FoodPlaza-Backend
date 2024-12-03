package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.exception.UnauthorizedException;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        // Delegar la creación del plato al handler
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

        // Delegar la actualización del plato al handler
        dishHandlerPort.updateDish(id, dishUpdateDto);
        log.info("Dish updated successfully.");
        return ResponseEntity.ok("Dish updated successfully.");
    }





}
