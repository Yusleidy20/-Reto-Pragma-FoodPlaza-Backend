package com.example.foodplaza.infrastructure.out.jpa.repository;

import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRestaurantRepositoryMySql extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByOwnerId(Long ownerId);

}
