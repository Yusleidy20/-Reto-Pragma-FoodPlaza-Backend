package com.example.foodplaza.infrastructure.out.jpa.repository;

import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByChefId(Long chefId);
    // Nuevo método para verificar pedidos activos
    List<OrderEntity> findByChefIdAndStateOrderIn(Long chefId, List<String> states);
    Page<OrderEntity> findByStateOrderAndRestaurant_IdRestaurant(String stateOrder, Long idRestaurant, Pageable pageable);
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderDishes WHERE o.stateOrder = :stateOrder AND o.restaurant.idRestaurant = :idRestaurant")
    Page<OrderEntity> findByStateOrderAndRestaurant_IdRestaurantWithDishes(@Param("stateOrder") String stateOrder, @Param("idRestaurant") Long idRestaurant, Pageable pageable);

}
