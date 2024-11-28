package com.example.foodplaza_users.domain.api;

import com.example.foodplaza_users.domain.model.UserRole;

import java.util.List;

public interface IRoleServicePort {
    void saveRole(UserRole userRole);
    List<UserRole> getAllRoles();
    UserRole getRole(Long idUserRole);
    void deleteRoleById(Long idUserRole);
}
