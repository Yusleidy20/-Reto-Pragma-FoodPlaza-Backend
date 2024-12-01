package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.RestaurantModel;

public interface IRestaurantPersistencePort {
    RestaurantModel saveRestaurant(RestaurantModel restaurant);

    RestaurantModel getRestaurantById(Long idRestaurant);

    RestaurantModel getRestaurantByIdOwner(Long ownerId);
}
