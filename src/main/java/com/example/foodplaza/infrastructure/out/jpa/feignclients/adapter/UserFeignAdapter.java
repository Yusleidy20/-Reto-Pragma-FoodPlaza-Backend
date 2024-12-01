package com.example.foodplaza.infrastructure.out.jpa.feignclients.adapter;

import com.example.foodplaza.domain.model.UserModel;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.infrastructure.configuration.IUserFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserDtoMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserFeignAdapter implements IUserFeignClientPort {
    private final IUserFeignClient userFeignClients;
    private final IUserDtoMapper userDtoMapper;
    @Override
    public Boolean existsUserById(Long userId) {
        return userFeignClients.existUserById(userId);
    }

    @Override
    public UserModel getUserById(Long userId) {
        UserDto userDto = userFeignClients.getUserById(userId);
        return userDtoMapper.toUserModel(userDto);
    }

    @Override
    public UserModel getUserByEmail(String email) {
        UserDto userDto = userFeignClients.getUserByEmail(email);
        return userDtoMapper.toUserModel(userDto);
    }
}
