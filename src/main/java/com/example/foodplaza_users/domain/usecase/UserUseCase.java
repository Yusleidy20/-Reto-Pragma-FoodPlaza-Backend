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
        validateUserDetails(userModel);
        assignRoleIfNeeded(userModel);
        encryptPassword(userModel);
        userPersistencePort.saveUser(userModel);
    }

    private void validateUserDetails(UserModel userModel) {
        validateBirthDate(userModel.getBirthDate());
        validateIfUserIsAdult(userModel);
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("The date of birth cannot be null.");
        }
    }

    private void validateIfUserIsAdult(UserModel userModel) {
        if (!isAdult(userModel.getBirthDate())) {
            throw new IllegalArgumentException("The user must be of legal age.");
        }
    }

    private boolean isAdult(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The date of birth cannot be in the future.");
        }
        Period age = Period.between(birthDate, LocalDate.now());
        return age.getYears() >= 18;
    }

    private void assignRoleIfNeeded(UserModel userModel) {
        if (userModel.getUserRole() != null && !userModel.getUserRole().getIdUserRole().equals(Constants.CUSTOMER_ROLE_ID)) {
            assignRole(userModel);
        } else {
            userModel.setUserRole(new UserRole(Constants.CUSTOMER_ROLE_ID, Constants.ROLE_CUSTOMER,Constants.ROLE_CUSTOMER_DESCRIPTION));
        }
    }

    private void assignRole(UserModel userModel) {
        Long idUserRole = userModel.getUserRole().getIdUserRole();
        if (idUserRole == null) {
            throw new IllegalArgumentException("The user role must be specified correctly.");
        }

        if (idUserRole.equals(Constants.EMPLOYEE_ROLE_ID)) {
            verifyOwnerPermissions();
            userModel.getUserRole().setNameRole(Constants.ROLE_EMPLOYEE);
        } else if (idUserRole.equals(Constants.ADMIN_ROLE_ID)) {
            verifyAdminPermissions();
            userModel.getUserRole().setNameRole(Constants.ROLE_ADMIN);
        } else if (idUserRole.equals(Constants.OWNER_ROLE_ID)) {
            verifyOwnerPermissions();
            userModel.getUserRole().setNameRole(Constants.ROLE_OWNER);
        } else {
            throw new IllegalArgumentException("Invalid role specified.");
        }
    }

    private void verifyOwnerPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isOwner = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Constants.ROLE_OWNER));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Constants.ROLE_ADMIN));

        if (!(isOwner || isAdmin)) {
            throw new UnauthorizedRoleException("Only a restaurant owner or administrator can create employee or owner accounts.");
        }
    }


    private void verifyAdminPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Constants.ROLE_ADMIN))) {
            throw new UnauthorizedRoleException("Only an administrator can create an admin role.");
        }
    }

    private void encryptPassword(UserModel userModel) {
        if (userModel.getPasswordUser() == null || userModel.getPasswordUser().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        String encryptedPassword = passwordEncoder.encode(userModel.getPasswordUser());
        userModel.setPasswordUser(encryptedPassword);
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
