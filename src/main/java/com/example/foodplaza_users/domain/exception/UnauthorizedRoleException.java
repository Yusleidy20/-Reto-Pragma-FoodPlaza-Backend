package com.example.foodplaza_users.domain.exception;

public class UnauthorizedRoleException extends RuntimeException {
    public UnauthorizedRoleException(String message){
        super(message);
    }
}
