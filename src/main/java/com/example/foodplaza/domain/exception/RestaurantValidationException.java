package com.example.foodplaza.domain.exception;

public class RestaurantValidationException extends RuntimeException{
    public  RestaurantValidationException(String message){
        super(message);
    }
}
