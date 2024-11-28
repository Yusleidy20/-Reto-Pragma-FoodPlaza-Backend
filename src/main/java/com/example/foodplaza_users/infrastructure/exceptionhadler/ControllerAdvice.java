package com.example.foodplaza_users.infrastructure.exceptionhadler;

import com.example.foodplaza_users.domain.exception.EmailExistException;
import com.example.foodplaza_users.domain.exception.InvalidEmailException;
import com.example.foodplaza_users.domain.exception.MissingFieldException;
import com.example.foodplaza_users.domain.exception.UserRoleNotFountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    private static final String MESSAGE = "message";

    // Manejo de excepciones específicas
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<Map<String, String>> handleMissingFieldException(MissingFieldException e) {
        return new ResponseEntity<>(Collections.singletonMap(MESSAGE, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmailException(InvalidEmailException e) {
        return new ResponseEntity<>(Collections.singletonMap(MESSAGE, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailExistException(EmailExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(UserRoleNotFountException.class)
    public ResponseEntity<Map<String, String>> handleUserRoleNotFoundException(UserRoleNotFountException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, e.getMessage()));
    }

    // Manejo de excepciones generales no específicas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(MESSAGE, "An unexpected error occurred: " + e.getMessage()));
    }
}
