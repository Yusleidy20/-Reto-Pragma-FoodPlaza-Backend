package com.example.foodplaza_users.infrastructure.out.jpa.adapter;


import com.example.foodplaza_users.domain.exception.UserNotFoundException;
import com.example.foodplaza_users.domain.exception.UserRoleNotFountException;

import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.domain.spi.persistence.IUserPersistencePort;


import com.example.foodplaza_users.infrastructure.out.jpa.entity.UserEntity;
import com.example.foodplaza_users.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.example.foodplaza_users.infrastructure.out.jpa.repository.IUserRepositoryMySQL;

import java.util.Optional;

public class UserPersistenceAdapterPortImpl implements IUserPersistencePort {
    private final IUserRepositoryMySQL userRepositoryMySQL;
    private final IUserEntityMapper userEntityMapper;
    public UserPersistenceAdapterPortImpl(IUserRepositoryMySQL userRepositoryMySQL, IUserEntityMapper userEntityMapper) {
        this.userRepositoryMySQL = userRepositoryMySQL;
        this.userEntityMapper = userEntityMapper;
    }


    @Override
    public void saveUser(UserModel userModel) {
        if (userModel.getUserRole() == null) {
            throw new IllegalStateException("The user role is not assigned correctly.");
        }

        UserEntity userEntity = userRepositoryMySQL.save(userEntityMapper.toUserEntity(userModel));
        userEntityMapper.toUserModel(userEntity);
    }


    @Override
    public UserModel findByEmail(String email) {
        Optional<UserEntity> userEntityOptional= userRepositoryMySQL.findByEmail(email);
        UserEntity userEntity = userEntityOptional.orElse(null);
        return userEntityMapper.toUserModel(userEntity);
    }

    @Override
    public UserModel findById(Long userId) {
        UserEntity userEntity = userRepositoryMySQL.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return userEntityMapper.toUserModel(userEntity);
    }




    @Override
    public boolean existById(Long userId) {
        try {
            return userRepositoryMySQL.existsById(userId);
        } catch (Exception e) {
            throw new UserRoleNotFountException("Error verifying user existence");
        }
    }

    @Override
    public boolean existByDocId(String docId) {
        return userRepositoryMySQL.existsByDocId(docId);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepositoryMySQL.existsByEmail(email);
    }


    @Override
    public void deleteUserById(Long userId) {
        userRepositoryMySQL.deleteById(userId);
    }
}
