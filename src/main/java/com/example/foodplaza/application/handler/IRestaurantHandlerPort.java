package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.RestaurantResponseDto;


import java.util.List;

public interface IRestaurantHandlerPort {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);

    RestaurantResponseDto getRestaurantById(Long idRestaurant);
    RestaurantResponseDto getRestaurantByIdOwner(Long ownerId);

    List<RestaurantResponseDto> getAllRestaurants();
    void deleteRestaurantById(Long idRestaurant);

}
