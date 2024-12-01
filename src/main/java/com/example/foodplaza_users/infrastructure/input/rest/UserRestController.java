package com.example.foodplaza_users.infrastructure.input.rest;



import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.application.dto.resquest.UserRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;
import com.example.foodplaza_users.domain.model.UserModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user-micro/user")
public class UserRestController {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);
    private final IUserServiceHandler userServiceHandler;
    private final IRoleServiceHandler roleServiceHandler;
    /**
     * Endpoint para registrar un Administrador.
     */
    @PostMapping("/admin")
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody UserRequestDto adminDto) {
        log.info("Inicio de registro de administrador: {}", adminDto.getEmail());

        try {
            userServiceHandler.saveUser(adminDto);
            log.info("Administrador registrado exitosamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully.");
        } catch (IllegalArgumentException e) {
            log.warn("Error de validation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al registrar administrador: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering admin.");
        }
    }

    /**
     * Endpoint para registrar un Owner, solo si el usuario que lo registra es un Administrador.
     */
    @PostMapping("/owner")
    public ResponseEntity<String> registerOwner(@RequestParam Long adminId, @Valid @RequestBody UserRequestDto ownerDto) {
        log.info("Inicio de registro de propietario. Admin ID: {}, Owner Email: {}", adminId, ownerDto.getEmail());

        try {
            if (!userServiceHandler.isAdmin(adminId)) {
                log.warn("El usuario con ID {} no tiene permisos de administrador.", adminId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must have Administrator role.");
            }

            userServiceHandler.saveUser(ownerDto);
            log.info("Propietario registrado exitosamente: {}", ownerDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Owner registered successfully.");
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al registrar propietario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering owner.");
        }
    }

    /**
     * Endpoint para obtener un usuario por ID.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        log.info("Buscando usuario por ID: {}", userId);

        try {
            UserResponseDto userResponse = userServiceHandler.getUserById(userId);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalStateException e) {
            log.warn("Usuario con ID {} no encontrado.", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error inesperado al buscar usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint para obtener un usuario por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        log.info("Buscando usuario por email: {}", email);

        try {
            UserResponseDto userResponse = userServiceHandler.getUserByEmail(email);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalStateException e) {
            log.warn("Usuario con email {} no encontrado.", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error inesperado al buscar usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint para verificar si existe un usuario por ID.
     */
    @GetMapping("/existUserById/{userId}")
    public ResponseEntity<Boolean> existUserById(@PathVariable Long userId) {
        log.info("Verificando existencia de usuario con ID: {}", userId);

        try {
            boolean exists = userServiceHandler.existsUserById(userId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error inesperado al verificar existencia de usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}

