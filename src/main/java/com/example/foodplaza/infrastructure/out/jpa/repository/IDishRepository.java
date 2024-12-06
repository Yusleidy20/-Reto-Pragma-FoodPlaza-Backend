package com.example.foodplaza.infrastructure.out.jpa.repository;

import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    @Query("SELECT d FROM DishEntity d WHERE d.idRestaurant.id = :idRestaurant AND d.idCategory.id = :idCategory")
    Page<DishEntity> findByIdRestaurantAndIdCategory(@Param("idRestaurant") Long idRestaurant, @Param("idCategory") Long idCategory, Pageable pageable);

    @Query("SELECT d FROM DishEntity d WHERE d.idRestaurant.id = :idRestaurant")
    Page<DishEntity> findByIdRestaurant(@Param("idRestaurant") Long idRestaurant, Pageable pageable);

}