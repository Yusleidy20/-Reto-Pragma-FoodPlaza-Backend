package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.handler.IRestaurantHandlerPort;

import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IRoleFeignClient;
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
public class RestaurantRestController {
    private final IRestaurantHandlerPort restaurantHandlerPort;
    private final IUserFeignClient userFeignClient;
    private static final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    private final IRoleFeignClient roleFeignClient;

    @PostMapping("/restaurant")
    public ResponseEntity<String> createRestaurant(
            @RequestParam Long adminId,
            @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        log.info("Starting restaurant registration. Admin ID: {}", adminId);
        log.info("Data received in RestaurantRequestDto: {}", restaurantRequestDto);

        // Validaciones iniciales
        if (restaurantRequestDto.getAddress() == null || restaurantRequestDto.getAddress().isEmpty()) {
            log.warn("Address not provided.");
            throw new IllegalArgumentException("Address is required.");
        }
        if (adminId == null || adminId <= 0) {
            log.warn("Invalid Admin ID.");
            throw new IllegalArgumentException("The administrator ID is invalid.");
        }

        // Llamada al handler
        restaurantHandlerPort.saveRestaurant(restaurantRequestDto);
        log.info("Restaurant registered successfully.");
        return new ResponseEntity<>("Restaurant created successfully.", HttpStatus.CREATED);
    }

}