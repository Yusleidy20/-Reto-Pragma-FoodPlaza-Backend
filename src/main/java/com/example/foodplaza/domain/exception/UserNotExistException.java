package com.example.foodplaza.domain.exception;

public class UserNotExistException extends  RuntimeException{
    public UserNotExistException(String message) {
        super(message); // Asegúrate de que el mensaje se pasa aquí
    }
}
