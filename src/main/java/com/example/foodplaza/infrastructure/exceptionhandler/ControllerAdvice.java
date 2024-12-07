package com.example.foodplaza.infrastructure.exceptionhandler;


import com.example.foodplaza.domain.exception.DishCreationException;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    private static final String MESSAGE_KEY = "message";

    // Manejo de excepciones de validación (DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }



    // Manejo de rutas no encontradas
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("Route not found: {}", ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE_KEY, "Route not found: " + ex.getRequestURL()));
    }

    // Manejo de recursos no encontrados
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

    // Manejo de argumentos ilegales
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

    // Manejo de acceso no autorizado
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Unauthorized access: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

    // Manejo de acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

    // Manejo de errores específicos de Hibernate (campos nulos)
    @ExceptionHandler(org.hibernate.PropertyValueException.class)
    public ResponseEntity<Map<String, String>> handlePropertyValueException(org.hibernate.PropertyValueException ex) {
        log.error("Persistence error: null or invalid field", ex);
        return ResponseEntity.badRequest()
                .body(Collections.singletonMap(MESSAGE_KEY, "Data error: Please ensure all required fields are complete."));
    }

    // Manejo de cualquier otra excepción
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(MESSAGE_KEY, "An unexpected error occurred: " + ex.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateKeyException(DataIntegrityViolationException e) {
        log.error("Database error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE_KEY, "Duplicate entry for key: " + e.getMessage()));
    }
    // Manejo de errores de estado ilegal
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        log.error("Business logic error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT) // Usamos 409 para conflictos de lógica de negocio
                .body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

}
