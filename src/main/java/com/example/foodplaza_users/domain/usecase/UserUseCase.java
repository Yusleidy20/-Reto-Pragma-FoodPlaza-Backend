package com.example.foodplaza_users.domain.usecase;

import com.example.foodplaza_users.domain.api.IUserServicePort;

import com.example.foodplaza_users.domain.model.UserRole;
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
        if (userModel.getUserRole() == null) {
            userModel.setUserRole(new UserRole());
        }
        UserRole userRole = userModel.getUserRole();
        if (userRole.getNameRole() == null) {
            throw new IllegalArgumentException("The user role must be specified correctly.");
        }

        if (userRole.getNameRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
            userRole.setIdUserRole(Constants.ADMIN_ROLE_ID);
        } else if (userRole.getNameRole().equalsIgnoreCase(Constants.ROLE_OWNER)) {
            userRole.setIdUserRole(Constants.OWNER_ROLE_ID);
        } else {
            throw new IllegalArgumentException("The role must be ADMINISTRATOR or OWNER..");
        }

        userModel.setUserRole(userRole);
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


