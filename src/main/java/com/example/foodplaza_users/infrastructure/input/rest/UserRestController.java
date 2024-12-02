package com.example.foodplaza_users.infrastructure.input.rest;



import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.application.dto.resquest.UserRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;

import com.example.foodplaza_users.domain.exception.UserRoleNotFountException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

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
        log.info("Admin Register: {}", adminDto.getEmail());
        userServiceHandler.saveUser(adminDto);
        log.info("Administrator registered successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully.");
    }

    /**
     * Endpoint para registrar un Owner, solo si el usuario que lo registra es un Administrador.
     */
    @PostMapping("/owner")
    public ResponseEntity<String> registerOwner(@RequestParam Long adminId, @Valid @RequestBody UserRequestDto ownerDto) {
        log.info("Owner registration start. Admin ID: {}, Owner Email: {}", adminId, ownerDto.getEmail());

        if (ownerDto.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("User must be an adult.");
        }
        if (!userServiceHandler.isAdmin(adminId)) {
            throw new UserRoleNotFountException("User must have Administrator role.");
        }

        userServiceHandler.saveUser(ownerDto);
        log.info("Owner registered successfully: {}", ownerDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Owner registered successfully.");
    }

    /**
     * Endpoint para obtener un usuario por ID.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        log.info("Searching for user by ID: {}", userId);
        UserResponseDto userResponse = userServiceHandler.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint para obtener un usuario por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        log.info("Searching for user by email: {}", email);
        UserResponseDto userResponse = userServiceHandler.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint para verificar si existe un usuario por ID.
     */
    @GetMapping("/existUserById/{userId}")
    public ResponseEntity<Boolean> existUserById(@PathVariable Long userId) {
        log.info("Verifying existence of user with ID: {}", userId);
        boolean exists = userServiceHandler.existsUserById(userId);
        return ResponseEntity.ok(exists);
    }
}

