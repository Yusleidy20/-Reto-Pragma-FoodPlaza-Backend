package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.infrastructure.configuration.IUserFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user-micro/foodplaza")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandlerPort dishHandlerPort;
    private final IUserFeignClient userFeignClient;
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    @PostMapping("/dish")
    public ResponseEntity<String> createDish(
            @Valid @RequestBody DishRequestDto dishRequestDto) {  // Aquí recibimos el DTO con todos los datos
        log.info("Datos recibidos en DishRequestDto: {}", dishRequestDto);

        // Validaciones iniciales
        if (dishRequestDto.getNameDish() == null || dishRequestDto.getNameDish().isEmpty()) {
            log.warn("Name no proporcionada.");
            return ResponseEntity.badRequest().body("El nombre del plato es requerido.");
        }
        if (dishRequestDto.getIdRestaurant() == null || dishRequestDto.getIdRestaurant() <= 0) {
            log.warn("ID del restaurante inválido.");
            return ResponseEntity.badRequest().body("El ID del restaurante es inválido.");
        }

        try {
            dishHandlerPort.createDish(dishRequestDto);
            log.info("Plato registrado exitosamente.");
            return new ResponseEntity<>("Plato creado exitosamente.", HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            log.error("Error: Restaurante no encontrado.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al registrar Plato", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Plato.");
        }
    }


}
