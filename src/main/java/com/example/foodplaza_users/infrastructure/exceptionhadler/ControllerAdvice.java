package com.example.foodplaza_users.infrastructure.exceptionhadler;

import com.example.foodplaza_users.domain.exception.*;
import com.example.foodplaza_users.infrastructure.exception.RoleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";  // New constant for "error"

    // Maneja excepciones de tipo MissingFieldException
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<Map<String, String>> handleMissingFieldException(MissingFieldException e) {
        return new ResponseEntity<>(Collections.singletonMap(MESSAGE, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones de tipo InvalidEmailException (cuando el email no es válido)
    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmailException(InvalidEmailException e) {
        return new ResponseEntity<>(Collections.singletonMap(MESSAGE, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones de tipo EmailExistException (cuando el correo ya existe)
    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailExistException(EmailExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, e.getMessage()));
    }

    // Maneja excepciones de tipo UserRoleNotFountException (cuando el rol de usuario no se encuentra)
    @ExceptionHandler(UserRoleNotFountException.class)
    public ResponseEntity<Map<String, String>> handleUserRoleNotFoundException(UserRoleNotFountException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, e.getMessage()));
    }

    // Maneja excepciones de validación de argumentos (por ejemplo, validaciones en los campos de un formulario)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // Maneja excepciones generales no capturadas por otros manejadores
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(MESSAGE, "An unexpected error occurred: " + e.getMessage()));
    }

    // Maneja excepciones de tipo BadCredentialsException (cuando las credenciales de autenticación son incorrectas)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(ERROR, "Invalid email or password"));
    }

    // Maneja excepciones de tipo UsernameNotFoundException (cuando no se encuentra al usuario)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(ERROR, "Email not found"));
    }

    // Maneja excepciones de tipo DataIntegrityViolationException (cuando hay una violación de integridad en la base de datos, como duplicados)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateKeyException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, "Duplicate entry for key: " + e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(ERROR, ex.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotFound(RoleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(ERROR, e.getMessage()));
    }
}
