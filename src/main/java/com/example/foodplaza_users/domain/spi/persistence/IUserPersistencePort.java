package com.example.foodplaza_users.domain.spi.persistence;

import com.example.foodplaza_users.domain.model.UserModel;

public interface IUserPersistencePort {
    UserModel saveUser(UserModel userModel);
    UserModel findByEmail(String email);
    UserModel findById(Long userId);

    boolean existById(Long userId);
    void deleteUserById(Long userId);
}
