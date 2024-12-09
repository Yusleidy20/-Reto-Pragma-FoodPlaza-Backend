package com.example.foodplaza.domain.exception;

public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException(String menssage){
        super(menssage);
    }
    }
