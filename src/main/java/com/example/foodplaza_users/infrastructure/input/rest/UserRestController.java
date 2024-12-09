package com.example.foodplaza_users.infrastructure.input.rest;



import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.application.dto.resquest.LoginRequest;
import com.example.foodplaza_users.application.dto.resquest.UserRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;


import com.example.foodplaza_users.infrastructure.configuration.Constants;
import com.example.foodplaza_users.infrastructure.configuration.segurity.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/user-micro/user")
public class UserRestController {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);
    private final IUserServiceHandler userServiceHandler;
    private final IRoleServiceHandler roleServiceHandler;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", token));
    }



    /**
     * Endpoint para registrar un Administrador.
     */
    @PostMapping("/admin")
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody UserRequestDto adminDto) {
        userServiceHandler.saveUser(adminDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully.");
    }

    /**
     * Endpoint para registrar un Owner, solo si el usuario que lo registra es un Administrador.
     */
    @PostMapping("/owner")
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<String> registerOwner(@Valid @RequestBody UserRequestDto ownerDto) {
        userServiceHandler.saveUser(ownerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Owner registered successfully.");
    }


    /**
     * Endpoint para registrar un Empleados, solo si el usuario que lo registra es un propietario.
     */
    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('Owner')")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody UserRequestDto employeeDto) {
        // Validar que el rol asignado sea empleado
        if (!employeeDto.getIdUserRole().equals(Constants.EMPLOYEE_ROLE_ID)) {
            throw new IllegalArgumentException("The role must be 'Employee'.");
        }
        userServiceHandler.saveUser(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee registered successfully.");
    }

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody UserRequestDto clientDto) {
        clientDto.setIdUserRole(Constants.CUSTOMER_ROLE_ID);
        userServiceHandler.saveUser(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully.");
    }


    /**
     * Endpoint para obtener un usuario por ID.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto userResponse = userServiceHandler.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint para obtener un usuario por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        UserResponseDto userResponse = userServiceHandler.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint para verificar si existe un usuario por ID.
     */
    @GetMapping("/existUserById/{userId}")
    public ResponseEntity<Boolean> existUserById(@PathVariable Long userId) {
        boolean exists = userServiceHandler.existsUserById(userId);
        return ResponseEntity.ok(exists);
    }
}

