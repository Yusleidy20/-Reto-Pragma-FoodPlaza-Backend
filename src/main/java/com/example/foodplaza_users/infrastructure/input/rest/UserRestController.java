package com.example.foodplaza_users.infrastructure.input.rest;



import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.application.dto.resquest.LoginRequest;
import com.example.foodplaza_users.application.dto.resquest.UserRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;


import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.infrastructure.configuration.Constants;
import com.example.foodplaza_users.infrastructure.configuration.segurity.UserDetailsImpl;
import com.example.foodplaza_users.infrastructure.configuration.segurity.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user-micro/user")
public class UserRestController {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);
    private final IUserServiceHandler userServiceHandler;
    private final IRoleServiceHandler roleServiceHandler;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());

        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Cargar los detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        // Extraer el userId desde el modelo de usuario
        UserModel userModel = ((UserDetailsImpl) userDetails).getUserModel();
        Long userId = userModel.getUserId();

        log.info("User authenticated: {}", userDetails.getUsername());
        log.info("Role assigned: {}", userDetails.getAuthorities().iterator().next().getAuthority());

        // Generar el token JWT
        String token = JwtUtil.generateToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority(),
                userId
        ); // Pasar el userId al token

        log.info("Generated token: {}", token);

        return ResponseEntity.ok(Map.of("token", token));
    }



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
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<String> registerOwner(@Valid @RequestBody UserRequestDto ownerDto) {

        if (ownerDto.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("User must be an adult.");
        }

        userServiceHandler.saveUser(ownerDto);
        log.info("Owner registered successfully: {}", ownerDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Owner registered successfully.");
    }


    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('Owner')")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody UserRequestDto employeeDto) {
        log.info("Employee Register: {}", employeeDto.getEmail());

        // Validar que el rol asignado sea empleado
        if (!employeeDto.getIdUserRole().equals(Constants.EMPLOYEE_ROLE_ID)) {
            throw new IllegalArgumentException("The role must be 'Employee'.");
        }

        // Delegar al servicio para guardar el empleado
        userServiceHandler.saveUser(employeeDto);
        log.info("Employee registered successfully: {}", employeeDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee registered successfully.");
    }

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody UserRequestDto clientDto) {
        log.info("Client Register: {}", clientDto.getEmail());

        // Configurar el rol automáticamente como "Cliente"
        clientDto.setIdUserRole(Constants.CUSTOMER_ROLE_ID);

        // Delegar al servicio para guardar el cliente
        userServiceHandler.saveUser(clientDto);

        log.info("Customer registered successfully: {}", clientDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully.");
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

