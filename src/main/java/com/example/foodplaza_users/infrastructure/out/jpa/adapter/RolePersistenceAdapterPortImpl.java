package com.example.foodplaza_users.infrastructure.out.jpa.adapter;

import com.example.foodplaza_users.domain.model.UserRole;
import com.example.foodplaza_users.domain.spi.persistence.IUserRolePersistencePort;
import com.example.foodplaza_users.infrastructure.exception.NoDataFoundException;
import com.example.foodplaza_users.infrastructure.exception.RoleNotFoundException;
import com.example.foodplaza_users.infrastructure.out.jpa.entity.RoleEntity;
import com.example.foodplaza_users.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.example.foodplaza_users.infrastructure.out.jpa.repository.IUserRoleRepositoryMySQL;

import java.util.List;
import java.util.Optional;

public class RolePersistenceAdapterPortImpl implements IUserRolePersistencePort {

    private final IUserRoleRepositoryMySQL userRoleRepositoryMySQL;
    private final IRoleEntityMapper roleEntityMapper;

    public RolePersistenceAdapterPortImpl(IUserRoleRepositoryMySQL userRoleRepositoryMySQL, IRoleEntityMapper roleEntityMapper) {
        this.userRoleRepositoryMySQL = userRoleRepositoryMySQL;
        this.roleEntityMapper = roleEntityMapper;
    }

    @Override
    public void saveRole(UserRole userRole) {
        RoleEntity roleEntity = roleEntityMapper.toEntity(userRole);
        if (roleEntity.getDescriptionRole() == null) {
            throw new IllegalArgumentException("descriptionRole cannot be null");
        }
        roleEntityMapper.toRoleModel(userRoleRepositoryMySQL.save(roleEntity));
    }



    @Override
    public List<UserRole> getAllRoles() {
        List<RoleEntity> roleEntities = userRoleRepositoryMySQL.findAll();
        if(roleEntities.isEmpty()){
            throw new NoDataFoundException();
        }
        return roleEntityMapper.toRoleList(roleEntities);
    }
    @Override
    public UserRole findByNameRole(String nameRole) {
        Optional<RoleEntity> roleEntity = userRoleRepositoryMySQL.findByNameRole(nameRole);
        return roleEntity.map(roleEntityMapper::toRoleModel).orElse(null);
    }


    @Override
    public UserRole findByRole(Long idUserRole) {
        return userRoleRepositoryMySQL.findById(idUserRole)
                .map(roleEntityMapper::toRoleModel)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + idUserRole));
    }


    @Override
    public UserRole findByUserId(Long userId) {
        Optional<RoleEntity> roleEntity = userRoleRepositoryMySQL.findByIdUserRole(userId);
        return roleEntity.map(roleEntityMapper::toRoleModel).orElse(null);
    }

    @Override
    public void deleteRoleById(Long userId) {
        userRoleRepositoryMySQL.deleteById(userId);
    }


}
