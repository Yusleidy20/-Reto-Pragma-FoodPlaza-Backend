package com.example.foodplaza_users.domain.usecase;

import com.example.foodplaza_users.domain.api.IUserServicePort;


import com.example.foodplaza_users.domain.exception.UnauthorizedRoleException;
import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.domain.model.UserRole;
import com.example.foodplaza_users.domain.spi.persistence.IUserPersistencePort;
import com.example.foodplaza_users.infrastructure.configuration.Constants;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public void saveUser(UserModel userModel) {
        if (userModel.getBirthDate() == null) {
            throw new IllegalArgumentException("The date of birth cannot be null.");
        }

        if (!isAdult(userModel.getBirthDate())) {
            throw new IllegalArgumentException("The user must be of legal age.");
        }

        // Validar y asignar rol si no es cliente
        if (userModel.getUserRole() != null && !userModel.getUserRole().getIdUserRole().equals(Constants.CUSTOMER_ROLE_ID)) {
            assignRole(userModel);
        } else {
            // Asignar rol de cliente automáticamente
            userModel.setUserRole(new UserRole(Constants.CUSTOMER_ROLE_ID, Constants.ROLE_CUSTOMER, "Client Role"));
        }

        // Encriptar la contraseña
        userModel.setPasswordUser(passwordEncoder.encode(userModel.getPasswordUser()));

        // Guardar el usuario
        userPersistencePort.saveUser(userModel);
    }

    private void assignRole(UserModel userModel) {
        if (userModel.getUserRole() == null || userModel.getUserRole().getIdUserRole() == null) {
            throw new IllegalArgumentException("The user role must be specified correctly.");
        }

        Long idUserRole = userModel.getUserRole().getIdUserRole();

        if (idUserRole.equals(Constants.EMPLOYEE_ROLE_ID)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(Constants.ROLE_OWNER))) {
                throw new UnauthorizedRoleException("Only a restaurant owner can create employee accounts.");
            }
            userModel.getUserRole().setNameRole(Constants.ROLE_EMPLOYEE);
        } else if (idUserRole.equals(Constants.ADMIN_ROLE_ID)) {
            userModel.getUserRole().setNameRole(Constants.ROLE_ADMIN);
        } else if (idUserRole.equals(Constants.OWNER_ROLE_ID)) {
            userModel.getUserRole().setNameRole(Constants.ROLE_OWNER);
        } else {
            throw new IllegalArgumentException("Invalid role specified.");
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


