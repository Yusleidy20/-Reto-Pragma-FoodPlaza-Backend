package com.example.foodplaza_users.infrastructure.out.jpa.repository;


import com.example.foodplaza_users.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepositoryMySQL extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long userId);
    boolean existsByEmail(String email);
    boolean existsByDocId(String docId);

}
