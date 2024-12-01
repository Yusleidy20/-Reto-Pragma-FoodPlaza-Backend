package com.example.foodplaza.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String menssage){
        super(menssage);
    }
}
