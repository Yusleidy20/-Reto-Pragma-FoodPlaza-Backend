package com.example.foodplaza.domain.exception;

public class DishCreationException extends RuntimeException {
    public DishCreationException(String message) {
        super(message);
    }

    public DishCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
