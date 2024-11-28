package com.example.foodplaza_users.application.handler;

import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.domain.model.UserModel;

public interface IUserServiceHandler {
    void saveUser(UserModel userRequestDto);
    UserModel getUserId(Long userId);
    Boolean existsUserById(Long id);

    UserResponseDto getUserById(Long userId);
    UserResponseDto getUserByEmail(String email);

}
