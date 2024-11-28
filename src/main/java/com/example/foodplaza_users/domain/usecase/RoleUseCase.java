package com.example.foodplaza_users.domain.usecase;

import com.example.foodplaza_users.domain.api.IRoleServicePort;
import com.example.foodplaza_users.domain.model.UserRole;
import com.example.foodplaza_users.domain.spi.persistence.IUserRolePersistencePort;

import java.util.List;

public class RoleUseCase implements IRoleServicePort {

    private final IUserRolePersistencePort userRolePersistencePort;

    public RoleUseCase(IUserRolePersistencePort userRolePersistencePort) {
        this.userRolePersistencePort = userRolePersistencePort;
    }

    @Override
    public void saveRole(UserRole userRole) {
        userRolePersistencePort.saveRole(userRole);
    }

    @Override
    public List<UserRole> getAllRoles() {
        return userRolePersistencePort.getAllRoles();
    }

    @Override
    public UserRole getRole(Long idUserRole) {
        return userRolePersistencePort.findByRole(idUserRole);
    }

    @Override
    public void deleteRoleById(Long idUserRole) {
        userRolePersistencePort.deleteRoleById(idUserRole);
    }
}
