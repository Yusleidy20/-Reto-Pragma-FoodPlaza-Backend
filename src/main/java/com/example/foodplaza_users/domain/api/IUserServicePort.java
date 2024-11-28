package com.example.foodplaza_users.domain.api;

import com.example.foodplaza_users.domain.model.UserModel;

public interface IUserServicePort {
    void saveUser(UserModel userModel);
    UserModel findByEmail(String email);
    UserModel findById(Long userId);
    boolean existUserById(Long userId);

}
