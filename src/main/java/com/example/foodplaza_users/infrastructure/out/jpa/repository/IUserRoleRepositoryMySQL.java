package com.example.foodplaza_users.infrastructure.out.jpa.repository;

import com.example.foodplaza_users.infrastructure.out.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRoleRepositoryMySQL extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByNameRole(String nameRole);
    Optional<RoleEntity> findByIdUserRole(Long idUserRole);  // Buscar por ID de rol

}
