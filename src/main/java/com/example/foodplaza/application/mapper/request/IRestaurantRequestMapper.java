package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface IRestaurantRequestMapper {

    RestaurantModel toRestaurant(RestaurantRequestDto restaurantRequestDto);

    RestaurantRequestDto toRestaurantRequest(RestaurantModel restaurantModel);
}
