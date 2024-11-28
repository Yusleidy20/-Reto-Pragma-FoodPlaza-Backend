package com.example.foodplaza_users.domain.exception;

public class UserRoleNotFountException extends RuntimeException{
    public UserRoleNotFountException(String message){
        super(message);
    }
}
