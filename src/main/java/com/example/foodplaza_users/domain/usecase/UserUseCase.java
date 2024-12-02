package com.example.foodplaza_users.domain.usecase;

import com.example.foodplaza_users.domain.api.IUserServicePort;


import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.domain.spi.persistence.IUserPersistencePort;
import com.example.foodplaza_users.infrastructure.configuration.Constants;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Period;


public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    private final PasswordEncoder passwordEncoder;


    public UserUseCase(IUserPersistencePort userPersistencePort, PasswordEncoder passwordEncoder) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;

    }

    public void saveUser(UserModel userModel) {

        if (userModel.getBirthDate() == null) {
            throw new IllegalArgumentException("The date of birth cannot be null.");
        }

        if (!isAdult(userModel.getBirthDate())) {
            throw new IllegalArgumentException("The user must be of legal age.");
        }

        assignRole(userModel);
        userModel.setPasswordUser(passwordEncoder.encode(userModel.getPasswordUser()));
        userPersistencePort.saveUser(userModel);
    }

    private void assignRole(UserModel userModel) {
        if (userModel.getUserRole() == null || userModel.getUserRole().getIdUserRole() == null) {
            throw new IllegalArgumentException("The user role must be specified correctly.");
        }

        Long idUserRole = userModel.getUserRole().getIdUserRole();
        if (idUserRole.equals(Constants.ADMIN_ROLE_ID)) {
            userModel.getUserRole().setNameRole(Constants.ROLE_ADMIN);
        } else if (idUserRole.equals(Constants.OWNER_ROLE_ID)) {
            userModel.getUserRole().setNameRole(Constants.ROLE_OWNER);
        } else {
            throw new IllegalArgumentException("The role must be ADMINISTRATOR or OWNER.");
        }
    }




    private boolean isAdult(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("The date of birth cannot be null.");
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The date of birth cannot be in the future.");
        }
        Period age = Period.between(birthDate, LocalDate.now());
        return age.getYears() >= 18;
    }



    @Override
    public UserModel findByEmail(String email) {
        return userPersistencePort.findByEmail(email);
    }

    @Override
    public UserModel findById(Long userId) {
        return userPersistencePort.findById(userId);
    }

    @Override
    public boolean existUserById(Long userId) {
        return userPersistencePort.existById(userId);
    }
}


