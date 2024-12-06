package com.example.foodplaza.infrastructure.out.jpa.repository;


import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByOwnerId(Long ownerId);

    Page<RestaurantEntity> findAllByOrderByNameRestaurantAsc(Pageable pageable);
    boolean existsByNit(String nit);

}
