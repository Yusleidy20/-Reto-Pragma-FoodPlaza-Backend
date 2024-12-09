package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;  // Cambio aquí

import java.util.List;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    RestaurantModel saveRestaurant(RestaurantModel restaurant);
    Page<RestaurantDto> getRestaurantsWithPaginationAndSorting(Pageable pageable);  // Cambio aquí
    RestaurantModel getRestaurantById(Long idRestaurant);
    RestaurantModel getRestaurantByIdOwner(Long ownerId);
    boolean existsByNit(String nit);
    Optional<RestaurantModel> findById(Long idRestaurant);
    List<RestaurantModel> getAllRestaurants();
    List<Long> getOrderIdsByRestaurantId(Long idRestaurant);
    boolean validateDishesBelongToRestaurant(Long idRestaurant, List<Long> dishIds);
}
