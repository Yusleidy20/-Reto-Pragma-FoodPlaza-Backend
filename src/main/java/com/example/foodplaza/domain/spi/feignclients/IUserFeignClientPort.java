package com.example.foodplaza.domain.spi.feignclients;

import com.example.foodplaza.domain.model.UserModel;

public interface IUserFeignClientPort {

    Boolean existsUserById(Long userId);
    UserModel getUserById(Long userId);
    UserModel getUserByEmail(String email);
}
