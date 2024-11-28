package com.example.foodplaza_users.domain.spi.persistence;

public interface IToken {
    String getBearerToken();

    String getEmail(String token);

    Long getUserAuthId(String token);

    String getUserAuthRol(String token);
}
