package com.example.foodplaza.domain.exception;

public class MissingFieldException extends RuntimeException{
    public MissingFieldException(String message){
        super(message);
    }
}
