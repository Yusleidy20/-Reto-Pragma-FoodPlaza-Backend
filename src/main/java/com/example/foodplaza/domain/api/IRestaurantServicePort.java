package com.example.foodplaza.domain.api;

import com.example.foodplaza.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantServicePort {

    void saveRestaurant(RestaurantModel restaurantModel);

    RestaurantModel getRestaurantById(Long idRestaurant);

    RestaurantModel getRestaurantByIdOwner(Long ownerId);

    List<RestaurantModel> getAllRestaurants();

    List<RestaurantModel> getRestaurantsWithPagination(Integer page, Integer size);

    void deleteRestaurantById(Long id);
}
