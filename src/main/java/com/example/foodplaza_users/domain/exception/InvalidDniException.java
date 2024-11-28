package com.example.foodplaza_users.domain.exception;

public class InvalidDniException extends RuntimeException{
    public InvalidDniException(String message){
        super(message);
    }
}
