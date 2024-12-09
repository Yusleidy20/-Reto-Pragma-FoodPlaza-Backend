package com.example.foodplaza.infrastructure.out.jpa.repository;


import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findById(Long idRestaurant);

    Optional<RestaurantEntity> findByOwnerId(Long ownerId);

    Page<RestaurantEntity> findAllByOrderByNameRestaurantAsc(Pageable pageable);
    boolean existsByNit(String nit);

    @Query("SELECT d.idDish FROM DishEntity d WHERE d.idRestaurant.idRestaurant = :idRestaurant")
    List<Long> findDishIdsByRestaurantId(@Param("idRestaurant") Long idRestaurant);
    @Query("SELECT o.id FROM OrderEntity o WHERE o.restaurant.id = :idRestaurant")
    List<Long> findOrderIdsByRestaurantId(@Param("idRestaurant") Long idRestaurant);

}
