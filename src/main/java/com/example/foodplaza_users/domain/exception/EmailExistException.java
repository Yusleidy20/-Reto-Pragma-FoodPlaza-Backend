package com.example.foodplaza_users.domain.exception;

public class EmailExistException extends RuntimeException {
    public EmailExistException(String message){
        super(message);
    }
}
