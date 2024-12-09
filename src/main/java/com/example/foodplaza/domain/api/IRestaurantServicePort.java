package com.example.foodplaza.domain.api;

import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRestaurantServicePort {

    void saveRestaurant(RestaurantModel restaurantModel);

    RestaurantModel getRestaurantById(Long idRestaurant);

    RestaurantModel getRestaurantByIdOwner(Long ownerId);

    List<RestaurantModel> getAllRestaurants();

    Page<RestaurantDto> getRestaurantsWithPaginationAndSorting(int page, int size, String sortBy);
    void deleteRestaurantById(Long id);
    List<Long> getOrderIdsByRestaurantId(Long idRestaurant);
}
