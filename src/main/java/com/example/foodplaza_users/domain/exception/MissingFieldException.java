package com.example.foodplaza_users.domain.exception;

public class MissingFieldException extends RuntimeException{
    public MissingFieldException(String message){
        super(message);
    }
}
