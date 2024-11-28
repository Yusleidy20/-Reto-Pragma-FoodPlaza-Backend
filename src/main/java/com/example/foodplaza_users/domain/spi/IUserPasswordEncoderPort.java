package com.example.foodplaza_users.domain.spi;

public interface IUserPasswordEncoderPort {
    String encode(String password);
}
