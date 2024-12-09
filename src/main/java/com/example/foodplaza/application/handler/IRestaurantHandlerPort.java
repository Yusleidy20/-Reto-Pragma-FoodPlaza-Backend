package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.application.dto.response.RestaurantResponseDto;
import org.springframework.data.domain.Page;


import java.util.List;

public interface IRestaurantHandlerPort {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    Page<RestaurantDto> getRestaurants(int page, int size);
    RestaurantResponseDto getRestaurantById(Long idRestaurant);
    List<Long> getOrderIdsByRestaurantId(Long idRestaurant);
    RestaurantResponseDto getRestaurantByIdOwner(Long ownerId);
    List<RestaurantResponseDto> getAllRestaurants();
    void deleteRestaurantById(Long idRestaurant);

}
