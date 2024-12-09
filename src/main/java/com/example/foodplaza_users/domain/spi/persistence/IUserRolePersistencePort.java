package com.example.foodplaza_users.domain.spi.persistence;

import com.example.foodplaza_users.domain.model.UserRole;


import java.util.List;

public interface IUserRolePersistencePort {
    void saveRole(UserRole userRole);
    List<UserRole> getAllRoles();
    UserRole findByNameRole(String nameRole);
    UserRole findByRole(Long idUserRole);
    UserRole findByUserId(Long userId);
    void deleteRoleById(Long userId);
}
