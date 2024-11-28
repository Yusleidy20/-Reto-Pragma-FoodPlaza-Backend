package com.example.foodplaza_users.infrastructure.input.rest;



import com.example.foodplaza_users.application.dto.response.UserResponseDto;
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
     * Endpoint para registrar un Administrador
     */
    @PostMapping("/admin")
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody UserModel admin) {
        if (admin.getBirthDate() == null) {
            return ResponseEntity.badRequest().body("The date of birth cannot be null.");
        }
        try {
            userServiceHandler.saveUser(admin);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Endpoint para registrar un Owner, solo si el usuario que lo registra es un Administrador
     */
    @PostMapping("/owner")
    public ResponseEntity<String> registerOwner(@RequestParam Long adminId, @RequestBody UserModel userModel) {

        UserModel admin = userServiceHandler.getUserId(adminId);

        // Verificar si el administrador tiene el rol correcto
        if (admin.getUserRole() == null || !admin.getUserRole().getNameRole().equals("Administrator")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user must have Administrator role.");
        }
        try {
            userServiceHandler.saveUser(userModel);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        try {
            // Delegar la lógica al servicio
            UserResponseDto userResponseDto = userServiceHandler.getUserById(userId);
            return ResponseEntity.ok(userResponseDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/email/{email}")
    public UserResponseDto getUserByEmail(@PathVariable String email) {
        return userServiceHandler.getUserByEmail(email);
    }
    @GetMapping("/existUserById/{userId}")
    public ResponseEntity<Boolean> existUserById(@PathVariable Long userId) {
        try {
            boolean exists = userServiceHandler.existsUserById(userId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


}


