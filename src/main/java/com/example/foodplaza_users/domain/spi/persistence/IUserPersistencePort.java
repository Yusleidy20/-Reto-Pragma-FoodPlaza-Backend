package com.example.foodplaza_users.domain.spi.persistence;

import com.example.foodplaza_users.domain.model.UserModel;

public interface IUserPersistencePort {
    void saveUser(UserModel userModel);
    UserModel findByEmail(String email);
    UserModel findById(Long userId);

    boolean existById(Long userId);
    boolean existByDocId(String docId);
    boolean existByEmail(String email);
    void deleteUserById(Long userId);
}
