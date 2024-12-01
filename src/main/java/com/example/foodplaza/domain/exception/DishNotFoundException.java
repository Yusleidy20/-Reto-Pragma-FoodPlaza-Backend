package com.example.foodplaza.domain.exception;

public class DishNotFoundException extends RuntimeException {
  public DishNotFoundException(String message) {
    super(message);
  }
}
