package com.example.foodplaza_users.application.handler.impl;

import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;
import com.example.foodplaza_users.application.mapper.request.IUserRequestMapper;
import com.example.foodplaza_users.application.mapper.response.IUserResponseMapper;
import com.example.foodplaza_users.domain.api.IRoleServicePort;
import com.example.foodplaza_users.domain.api.IUserServicePort;

import com.example.foodplaza_users.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceHandlerImpl implements IUserServiceHandler {
    private final IUserServicePort userServicePort;
    private final IRoleServicePort roleServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;
    private static final Logger log = LoggerFactory.getLogger(UserServiceHandlerImpl.class);

    @Override
    public void saveUser(UserModel userModel) {
        UserModel userMo = userRequestMapper.toUser(userModel);
        userServicePort.saveUser(userMo);
    }

    @Override
    public UserModel getUserId(Long userId) {
        return userServicePort.findById(userId);
    }

    @Override
    public Boolean existsUserById(Long userId) {
        return userServicePort.existUserById(userId);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        // Obtén el modelo completo
        UserModel userModel = userServicePort.findById(userId);

        // Mapea el modelo a DTO
        UserResponseDto userResponseDto = userResponseMapper.toUserResponse(userModel);

        // Log para depuración
        log.info("Usuario mapeado a DTO con rol: {}", userResponseDto);

        return userResponseDto;
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        return userResponseMapper.toUserResponse(userServicePort.findByEmail(email));
    }



}
