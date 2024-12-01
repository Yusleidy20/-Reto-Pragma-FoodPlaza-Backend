package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.handler.IRestaurantHandlerPort;

import com.example.foodplaza.infrastructure.configuration.IRoleFeignClient;
import com.example.foodplaza.infrastructure.configuration.IUserFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
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

    private final IRoleFeignClient roleFeignClient; // Asegúrate de tener este FeignClient inyectado

    @PostMapping("/restaurant")
    public ResponseEntity<String> createRestaurant(
            @RequestParam Long adminId,
            @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        log.info("Iniciando registro de restaurante. Admin ID: {}", adminId);
        log.info("Datos recibidos en RestaurantRequestDto: {}", restaurantRequestDto);

        // Validaciones iniciales
        if (restaurantRequestDto.getAddress() == null || restaurantRequestDto.getAddress().isEmpty()) {
            log.warn("Dirección no proporcionada.");
            return ResponseEntity.badRequest().body("La dirección es requerida.");
        }
        if (adminId == null || adminId <= 0) {
            log.warn("Admin ID inválido.");
            return ResponseEntity.badRequest().body("El ID del administrador es inválido.");
        }

        try {
            // Llamada al handler
            restaurantHandlerPort.saveRestaurant(restaurantRequestDto);
            log.info("Restaurante registrado exitosamente.");
            return new ResponseEntity<>("Restaurante creado exitosamente.", HttpStatus.CREATED);
        } catch (org.hibernate.PropertyValueException e) {
            log.error("Error en persistencia: campo nulo o inválido", e);
            return ResponseEntity.badRequest().body("Error en los datos: asegúrate de que todos los campos requeridos estén completos.");
        } catch (Exception e) {
            log.error("Error inesperado al registrar restaurante", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el restaurante.");
        }
    }
}